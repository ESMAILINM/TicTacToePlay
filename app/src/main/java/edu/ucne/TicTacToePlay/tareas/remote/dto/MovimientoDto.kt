package edu.ucne.TicTacToePlay.tareas.remote.dto

data class MovimientoDto (
    val partidaId: Int? = null,
    val jugador: String = "",
    val posicionFila: Int,
    val posicionColumna: Int
)

