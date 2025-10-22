package edu.ucne.TicTacToePlay.tareas.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logros")
data class LogroEntity (
    @PrimaryKey(autoGenerate = true)
    val logroId: Int = 0,
    val nombre: String,
    val descripcion: String
    )