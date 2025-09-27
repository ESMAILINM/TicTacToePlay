package edu.ucne.TicTacToePlay.domain.model

import edu.ucne.TicTacToePlay.utils.getFechaActual

data class Partida(
    val partidaId: Int = 0,
    val fecha: String = getFechaActual(),
    val jugador1Id: Int,
    val jugador2Id: Int,
    val ganadorId: Int?= null,
    val esFinalizada: Boolean = false
)