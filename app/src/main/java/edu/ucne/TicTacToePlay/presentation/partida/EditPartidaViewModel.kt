package edu.ucne.TicTacToePlay.presentation.partidas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase.DeletePartidaUseCase
import edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase.GetPartidaUseCase
import edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase.ObservePartidaUseCase
import edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase.UpsertPartidaUseCase
import edu.ucne.TicTacToePlay.domain.validation.PartidaValidator
import edu.ucne.TicTacToePlay.presentation.partida.EditPartidaUiEvent
import edu.ucne.TicTacToePlay.presentation.partida.EditPartidaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditPartidaViewModel(
    private val upsertPartidaUseCase: UpsertPartidaUseCase,
    private val getPartidaUseCase: GetPartidaUseCase,
    private val deletePartidaUseCase: DeletePartidaUseCase,
    private val observePartidaUseCase: ObservePartidaUseCase
) : ViewModel() {

    private val validator = PartidaValidator()

    private val _state = MutableStateFlow(EditPartidaUiState())
    val state: StateFlow<EditPartidaUiState> = _state.asStateFlow()

    fun onEvent(event: EditPartidaUiEvent) {
        when (event) {
            is EditPartidaUiEvent.Load -> loadPartida(event.partidaId)
            is EditPartidaUiEvent.FechaChanged -> _state.update { it.copy(fecha = event.fecha, fechaError = null) }
            is EditPartidaUiEvent.Jugador1Changed -> _state.update { it.copy(jugador1Id = event.jugadorId, jugador1Error = null) }
            is EditPartidaUiEvent.Jugador2Changed -> _state.update { it.copy(jugador2Id = event.jugadorId, jugador2Error = null) }
            is EditPartidaUiEvent.GanadorChanged -> _state.update { it.copy(ganadorId = event.ganadorId) }
            is EditPartidaUiEvent.EsFinalizadaChanged -> _state.update { it.copy(esFinalizada = event.finalizada) }
            EditPartidaUiEvent.Save -> savePartida()
            EditPartidaUiEvent.Delete -> deletePartida()
        }
    }

    private fun loadPartida(partidaId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            getPartidaUseCase(partidaId).onSuccess { partida ->
                partida?.let {
                    _state.update { state ->
                        state.copy(
                            partidaId = it.partidaId,
                            fecha = it.fecha,
                            jugador1Id = it.jugador1Id,
                            jugador2Id = it.jugador2Id,
                            ganadorId = it.ganadorId,
                            esFinalizada = it.esFinalizada,
                            isNew = false,
                            isSaving = false
                        )
                    }
                } ?: _state.update { it.copy(isSaving = false) }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun savePartida() {
        val current = _state.value

        // Validaciones
        val jugadoresValid = validator.validateJugadores(current.jugador1Id, current.jugador2Id)
        val fechaValid = validator.validateFecha(current.fecha)
        val ganadorValid = validator.validateGanador(current.ganadorId, current.jugador1Id, current.jugador2Id)

        if (!jugadoresValid.isValid || !fechaValid.isValid || !ganadorValid.isValid) {
            _state.update {
                it.copy(
                    jugador1Error = jugadoresValid.error,
                    jugador2Error = jugadoresValid.error,
                    fechaError = fechaValid.error,
                    ganadorError = ganadorValid.error
                )
            }
            return
        }

        val partida = Partida(
            partidaId = current.partidaId ?: 0,
            fecha = current.fecha,
            jugador1Id = current.jugador1Id,
            jugador2Id = current.jugador2Id,
            ganadorId = current.ganadorId,
            esFinalizada = current.esFinalizada
        )

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            upsertPartidaUseCase(partida).onSuccess { id ->
                _state.update { it.copy(isSaving = false, saved = true, partidaId = id, isNew = false) }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun deletePartida() {
        val id = _state.value.partidaId ?: return

        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deletePartidaUseCase(id).onSuccess {
                _state.update { it.copy(isDeleting = false, deleted = true) }
            }.onFailure {
                _state.update { it.copy(isDeleting = false) }
            }
        }
    }
}
