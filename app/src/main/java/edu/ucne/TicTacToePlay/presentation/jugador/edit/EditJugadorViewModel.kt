package edu.ucne.TicTacToePlay.presentation.jugador.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase.DeleteJugadorUseCase
import edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase.GetJugadorUseCase
import edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase.TriggerSyncUseCase
import edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase.UpsertJugadorUseCase
//import edu.ucne.TicTacToePlay.domain.validation.JugadorValidator
import edu.ucne.TicTacToePlay.tareas.remote.JugadorRemoteDataSource
import edu.ucne.TicTacToePlay.tareas.remote.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
@HiltViewModel
class EditJugadorViewModel @Inject constructor(
    private val upsertJugadorUseCase: UpsertJugadorUseCase,
    private val getJugadorUseCase: GetJugadorUseCase,
    private val deleteJugadorUseCase: DeleteJugadorUseCase,
//    private val jugadorValidator: JugadorValidator,
    private val triggerSyncUseCase: TriggerSyncUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditJugadorUiState())
    val state: StateFlow<EditJugadorUiState> = _state.asStateFlow()

    fun onEvent(event: EditJugadorEvent) {
        when (event) {
            is EditJugadorEvent.Load -> onLoad(event.id)
            is EditJugadorEvent.NombresChanged -> _state.update { it.copy(nombres = event.nombres, nombreError = null) }
            is EditJugadorEvent.EmailChanged -> _state.update { it.copy(email = event.email, emailError = null) }
            EditJugadorEvent.Save -> onSave()
            is EditJugadorEvent.Delete -> onDelete(event.id)
            EditJugadorEvent.ShowCreateSheet -> _state.update { it.copy(showCreateSheet = true) }
            EditJugadorEvent.HideCreateSheet -> _state.update { it.copy(showCreateSheet = false) }
            EditJugadorEvent.UserMessageShown -> clearMessage()
            is EditJugadorEvent.CrearJugador -> crearJugador(event.nombres, event.partidas)
            is EditJugadorEvent.UpdateJugador -> saveJugador(event.jugador)

            else -> Unit
        }
    }
    private fun crearJugador(nombres: String, partidas: Int) {
        viewModelScope.launch {
            val jugador = Jugador(
                jugadorId = UUID.randomUUID().toString(),
                nombres = nombres,
                email = "",
            )

            when (val result = upsertJugadorUseCase(jugador)) {
                is Resource.Success -> _state.update { it.copy(isSaved = true, jugadorId = jugador.jugadorId) }
                is Resource.Error -> _state.update { it.copy(errorMessage = result.message) }
                is Resource.Loading -> _state.update { it.copy(isSaving = true) }
            }

            try {
                triggerSyncUseCase()
            } catch (e: Exception) {
                _state.update { it.copy(userMessage = "Error al sincronizar: ${e.message}") }
            }
        }
    }
    private fun saveJugador(jugador: Jugador) {
        viewModelScope.launch {
            when (val result = upsertJugadorUseCase(jugador)) {
                is Resource.Success -> _state.update { it.copy(isSaved = true, jugadorId = jugador.jugadorId) }
                is Resource.Error -> _state.update { it.copy(errorMessage = result.message) }
                is Resource.Loading -> _state.update { it.copy(isSaving = true) }
            }
            try {
                triggerSyncUseCase()
            } catch (e: Exception) {
                _state.update { it.copy(userMessage = "Error al sincronizar: ${e.message}") }
            }
        }
    }



    private fun onLoad(id: String?) {
        if (id == null) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val jugador = getJugadorUseCase(id)
            _state.update {
                if (jugador != null) {
                    it.copy(
                        jugadorId = jugador.jugadorId,
                        nombres = jugador.nombres,
                        email = jugador.email ?: "",
                        isLoading = false,
                        canBeDeleted = true
                    )
                } else {
                    it.copy(isLoading = false, errorMessage = "Jugador no encontrado")
                }
            }
        }
    }

    private fun onSave() {
        val nombres = state.value.nombres
        val email = state.value.email
        val currentJugadorId = state.value.jugadorId

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, nombreError = null, emailError = null, errorMessage = null) }

//            val nombreValidation = jugadorValidator.validateNombre(nombres)
//            val nombreUnicoValidation = jugadorValidator.validateNombreUnico(nombres, currentJugadorId)

//            if (!nombreValidation.isValid || !nombreUnicoValidation.isValid) {
//                _state.update {
//                    it.copy(
//                        isSaving = false,
//                        nombreError = nombreValidation.error ?: nombreUnicoValidation.error,
//                        errorMessage = "Por favor, corrija los errores en el formulario."
//                    )
//                }
//                return@launch
//            }

            val jugador = Jugador(
                jugadorId = currentJugadorId ?: java.util.UUID.randomUUID().toString(),
                nombres = nombres,
                email = email
            )

            when (val result = upsertJugadorUseCase(jugador)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            isSaved = true,
                            jugadorId = jugador.jugadorId,
                            errorMessage = null
                        )
                    }
                    try {
                        triggerSyncUseCase()
                    } catch (e: Exception) {
                        _state.update { it.copy(userMessage = "Error al sincronizar: ${e.message}") }
                    }
                }
                is Resource.Error -> _state.update { it.copy(isSaving = false, errorMessage = result.message) }
                is Resource.Loading -> _state.update { it.copy(isSaving = true) }
            }
        }
    }

    private fun onDelete(id: String) {
        if (id.isEmpty()) {
            _state.update { it.copy(errorMessage = "No se puede eliminar un jugador sin ID válido") }
            return
        }
        _state.update { it.copy(isDeleting = true, isSaved = false, errorMessage = null) }
        viewModelScope.launch {
            try {
                deleteJugadorUseCase(id)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        isSaved = true,
                        errorMessage = null,
                        jugadorId = null,
                        nombres = "",
                        email = "",
                        canBeDeleted = false
                    )
                }
                try {
                    triggerSyncUseCase()
                } catch (e: Exception) {
                    _state.update { it.copy(userMessage = "Error al sincronizar: ${e.message}") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, errorMessage = e.message ?: "Error desconocido al eliminar el jugador") }
            }
        }
    }

    private fun clearMessage() {
        _state.update { it.copy(userMessage = null) }
    }
}
