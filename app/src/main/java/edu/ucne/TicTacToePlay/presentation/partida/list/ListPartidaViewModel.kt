package edu.ucne.TicTacToePlay.presentation.partida.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPartidaViewModel @Inject constructor(
    private val partidaRepository: PartidaRepository,
    private val jugadorRepository: JugadorRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ListPartidaUiState(isLoading = true))
    val state: StateFlow<ListPartidaUiState> = _state.asStateFlow()

    init {
        loadPartidas()
        loadJugadores()
        insertJugadoresPrueba()
    }

    private fun loadPartidas() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            partidaRepository.observePartidas().collect { lista ->
                _state.update { it.copy(partidas = lista, isLoading = false) }
            }
        }
    }

    private fun loadJugadores() {
        viewModelScope.launch {
            jugadorRepository.observeJugador().collect { lista ->
                _state.update { it.copy(jugadores = lista) }
            }
        }
    }

    fun onEvent(event: ListPartidaUiEvent) {
        when (event) {
            is ListPartidaUiEvent.OnDeletePartidaClick -> deletePartida(event.partida)
            is ListPartidaUiEvent.OnseleccionarJugadores -> selectJugador(event.jugador)
        }
    }

    private fun deletePartida(partida: Partida) {
        viewModelScope.launch {
            partidaRepository.delete(partida.partidaId)
            loadPartidas()
        }
    }

    private fun selectJugador(jugador: Jugador) {
        _state.update { it.copy(jugadorSeleccionado = jugador) }
    }

    private fun insertJugadoresPrueba() {
        viewModelScope.launch {
            val jugadoresExistentes = jugadorRepository.observeJugador().first()
            if (jugadoresExistentes.isEmpty()) {
                listOf("Juan", "Ana", "Luis").forEach { nombre ->
                    jugadorRepository.upsertJugador(Jugador(nombres = nombre, partidas = 6))
                }
            }
        }
    }
}
