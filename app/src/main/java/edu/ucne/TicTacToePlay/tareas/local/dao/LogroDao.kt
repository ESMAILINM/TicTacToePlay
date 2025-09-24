package edu.ucne.TicTacToePlay.tareas.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.TicTacToePlay.tareas.local.entities.LogroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogroDao {
    @Query("SELECT * FROM logros ORDER BY logroId DESC")
    fun observeAll(): Flow<List<LogroEntity>>

    @Query("SELECT * FROM logros WHERE logroId = :id")
    suspend fun getById(id: Int): LogroEntity?

    @Upsert
    suspend fun upsert(entity: LogroEntity): Long

    @Delete
    suspend fun delete(entity: LogroEntity)

    @Query("DELETE FROM logros WHERE logroId = :id")
    suspend fun deleteById(id: Int)

}