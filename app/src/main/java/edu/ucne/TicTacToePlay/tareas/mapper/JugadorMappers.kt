package edu.ucne.TicTacToePlay.tareas.mapper

import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.tareas.local.entities.JugadorEntity
import edu.ucne.TicTacToePlay.tareas.remote.dto.JugadorRequest
import edu.ucne.TicTacToePlay.tareas.remote.dto.JugadorResponse


fun JugadorEntity.toDomain(): Jugador = Jugador(
    jugadorId = jugadorId,
    remoteId = remoteId,
    nombres = nombres,
    email = email,
    isPendingCreate = isPendingCreate
)

fun Jugador.toEntity(): JugadorEntity = JugadorEntity(
    jugadorId = jugadorId,
    remoteId = remoteId,
    nombres = nombres,
    email = email,
    isPendingCreate = isPendingCreate
)

fun JugadorResponse.toDomain(): Jugador = Jugador(
    jugadorId = jugadorId.toString(),
    remoteId = jugadorId,
    nombres = nombres,
    email = email
)

fun Jugador.toRequest(): JugadorRequest = JugadorRequest(
    nombres = nombres,
    email = email
)

