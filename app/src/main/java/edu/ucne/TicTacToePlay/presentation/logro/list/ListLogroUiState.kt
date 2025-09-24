package edu.ucne.TicTacToePlay.presentation.logro.list

import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.model.Logro

data class ListLogroUiState(
    val logros: List<Logro> = emptyList(),
    val isLoading: Boolean = true
)
