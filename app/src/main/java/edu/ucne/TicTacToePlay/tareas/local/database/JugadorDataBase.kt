package edu.ucne.TicTacToePlay.tareas.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.TicTacToePlay.tareas.local.dao.JugadorDao
import edu.ucne.TicTacToePlay.tareas.local.dao.PartidaDao
import edu.ucne.TicTacToePlay.tareas.local.entities.JugadorEntity
import edu.ucne.TicTacToePlay.tareas.local.entities.PartidaEntity

@Database(
    entities = [
        JugadorEntity::class,
        PartidaEntity::class
    ],
    version = 3,
    exportSchema = false,
)
abstract class JugadorDataBase : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
    abstract fun partidaDao(): PartidaDao
}