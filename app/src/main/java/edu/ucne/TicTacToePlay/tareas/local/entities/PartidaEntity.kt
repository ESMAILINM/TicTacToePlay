package edu.ucne.TicTacToePlay.tareas.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Partidas")
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true)
    val partidaId: Int = 0,
    val fecha: String,
    val jugador1Id: Int,
    val jugador2Id: Int,
    val ganadorId: Int?,
    val esFinalizada: Boolean = false,
)


