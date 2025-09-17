package edu.ucne.TicTacToePlay.presentation.jugador.list

import edu.ucne.TicTacToePlay.domain.model.Jugador

data class ListJugadorUiState(
val jugadores: List<Jugador> = emptyList(),
val isLoading: Boolean = true
)

