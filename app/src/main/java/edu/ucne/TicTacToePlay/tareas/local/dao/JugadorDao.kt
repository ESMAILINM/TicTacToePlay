// kotlin
// File: app/src/main/java/edu/ucne/TicTacToePlay/tareas/local/dao/JugadorDao.kt
package edu.ucne.TicTacToePlay.tareas.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.TicTacToePlay.tareas.local.entities.JugadorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {
    @Query("SELECT * FROM Jugadores ORDER BY jugadorId DESC")
    fun observeAll(): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM Jugadores WHERE jugadorId = :id")
    suspend fun getById(id: String): JugadorEntity?

    @Upsert
    suspend fun upsert(jugador: JugadorEntity)

    @Delete
    suspend fun delete(jugador: JugadorEntity)

    @Query("DELETE FROM Jugadores WHERE jugadorId = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM Jugadores WHERE nombres COLLATE NOCASE = :name LIMIT 1")
    suspend fun getJugadorByName(name: String): JugadorEntity?

    @Query("SELECT * FROM Jugadores WHERE isPendingCreate = 1")
    suspend fun getJugadoresPendingCreate(): List<JugadorEntity>
}
