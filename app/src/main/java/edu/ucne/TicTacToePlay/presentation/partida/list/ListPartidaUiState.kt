package edu.ucne.TicTacToePlay.presentation.partida.list

import edu.ucne.TicTacToePlay.domain.model.Partida


data class ListPartidaUiState(
    val partidas: List<Partida> = emptyList(),
    val isLoading: Boolean = false
)