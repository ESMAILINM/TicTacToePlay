package edu.ucne.TicTacToePlay.presentation.partida.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListPartidaViewModel @Inject constructor(
    private val repository: PartidaRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ListPartidaUiState(isLoading = true))
    val state: StateFlow<ListPartidaUiState> = _state.asStateFlow()

    init {
        loadPartidas()
    }

    private fun loadPartidas() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val partidas = repository.observePartidas() // Flow<List<Partida>>
            partidas.collect { lista ->
                _state.update { it.copy(partidas = lista, isLoading = false) }
            }
        }
    }

    fun onEvent(event: ListPartidaUiEvent) {
        when (event) {
            is ListPartidaUiEvent.OnDeletePartidaClick -> deletePartida(event.partida)
        }
    }

    private fun deletePartida(partida: Partida) {
        viewModelScope.launch {
            repository.delete(partida.partidaId)
            // Opcional: refrescar la lista después de borrar
            loadPartidas()
        }
    }
}