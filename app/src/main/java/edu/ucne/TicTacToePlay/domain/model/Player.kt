package edu.ucne.TicTacToePlay.domain.model

sealed class Player(val symbol: String) {
    object X : Player("X")
    object O : Player("O")
}