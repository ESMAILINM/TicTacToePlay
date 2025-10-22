package edu.ucne.TicTacToePlay.presentation.tictactoe

import edu.ucne.TicTacToePlay.domain.model.Player

data class GameUiState(
    val board: List<Player?> = List(9) { null },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isDraw: Boolean = false,
    val gameStarted: Boolean = false,
    val playerSelection: Player? = null
)