package edu.ucne.TicTacToePlay.domain.repository

import edu.ucne.TicTacToePlay.domain.model.Logro
import kotlinx.coroutines.flow.Flow

interface LogroRepository {
    fun observeLogros(): Flow<List<Logro>>

    suspend fun getLogro(id: Int): Logro?

    suspend fun upsertLogro(Logro: Logro): Int

    suspend fun deleteLogro(id: Int)
}