package edu.ucne.TicTacToePlay.tareas.mapper

import edu.ucne.TicTacToePlay.domain.model.Logro
import edu.ucne.TicTacToePlay.tareas.local.entities.LogroEntity

fun LogroEntity.toDomain(): Logro= Logro(
    logroId = logroId,
    nombre = nombre,
    descripcion = descripcion
)

fun Logro.toEntity(): LogroEntity = LogroEntity(
    logroId = logroId,
    nombre = nombre,
    descripcion = descripcion
)