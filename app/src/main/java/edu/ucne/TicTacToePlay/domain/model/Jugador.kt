package edu.ucne.TicTacToePlay.domain.model

import java.util.UUID

data class Jugador(
    val jugadorId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombres: String,
    val email : String = "",
    val isPendingCreate: Boolean = false
)
