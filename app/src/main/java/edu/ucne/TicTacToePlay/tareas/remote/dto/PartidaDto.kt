package edu.ucne.TicTacToePlay.tareas.remote.dto

data class PartidaDto (
    val partidaId: Int = 0,
    val jugador1Id: Int,
    val jugador2Id: Int,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false
)