package edu.ucne.TicTacToePlay.domain.model

data class Partida(
    val partidaId: Int = 0,
    val fecha: String = "",
    val jugador1Id: Int,
    val jugador2Id: Int,
    val ganadorId: Int?= null,
    val esFinalizada: Boolean = false
)