package edu.ucne.TicTacToePlay.tareas.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

import edu.ucne.TicTacToePlay.tareas.local.entities.PartidaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {

    @Query("SELECT * FROM partidas ORDER BY partidaId DESC")
    fun observeAll(): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM partidas WHERE partidaId = :id")
    suspend fun getById(id: Int): PartidaEntity?

    @Upsert
    suspend fun upsert(entity: PartidaEntity): Long


    @Delete
    suspend fun delete(entity: PartidaEntity)

    @Query("DELETE FROM partidas WHERE partidaId = :id")
    suspend fun deleteById(id: Int)
}