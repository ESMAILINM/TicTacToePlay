package edu.ucne.TicTacToePlay.tareas.mapper
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.tareas.local.entities.PartidaEntity

fun PartidaEntity.toDomain(): Partida =  Partida(
        partidaId = partidaId,
        fecha = fecha,
        jugador1Id = jugador1Id,
        jugador2Id = jugador2Id,
        ganadorId = ganadorId,
        esFinalizada = esFinalizada
    )

fun Partida.toEntity(): PartidaEntity = PartidaEntity(
        partidaId = partidaId,
        fecha = fecha,
        jugador1Id = jugador1Id,
        jugador2Id = jugador2Id,
        ganadorId = ganadorId,
        esFinalizada = esFinalizada
    )