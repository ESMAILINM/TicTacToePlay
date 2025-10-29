package edu.ucne.TicTacToePlay.presentation.jugador.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase.DeleteJugadorUseCase
import edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase.ObserveJugadorUseCase
import edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase.TriggerSyncUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class JugadorListViewModel @Inject constructor(
    private val deleteJugadorUseCase: DeleteJugadorUseCase,
    private val observeJugadorUseCase: ObserveJugadorUseCase,
    private val triggerSyncUseCase: TriggerSyncUseCase
) : ViewModel() {

    private val _userMessage = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val userMessage = _userMessage

    val state: StateFlow<ListJugadorUiState> =
        observeJugadorUseCase()
            .map { jugadores ->
                ListJugadorUiState(jugadores = jugadores, isLoading = false)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                ListJugadorUiState(isLoading = true)
            )

    fun startSync() {
        viewModelScope.launch {
            try {
                triggerSyncUseCase()
                _userMessage.emit("Sincronización en segundo plano iniciada")
            } catch (e: Exception) {
                _userMessage.emit("Error al iniciar sincronización: ${e.message}")
            }
        }
    }

    fun onEvent(event: ListJugadorUiEvent) {
        when (event) {
            is ListJugadorUiEvent.OnDeleteJugadorClick -> deleteJugador(event.jugador.jugadorId)
            is ListJugadorUiEvent.Delete -> deleteJugador(event.id.toString())
            else -> Unit
        }
    }

    private fun deleteJugador(id: String) {
        if (id.isEmpty()) {
            viewModelScope.launch { _userMessage.emit("No se puede eliminar un jugador sin ID válido") }
            return
        }

        viewModelScope.launch {
            try {
                deleteJugadorUseCase(id)
                triggerSyncUseCase()
                _userMessage.emit("Jugador eliminado correctamente")
            } catch (e: Exception) {
                _userMessage.emit("Error al eliminar jugador: ${e.message}")
            }
        }
    }
}
