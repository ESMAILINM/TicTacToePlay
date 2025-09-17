package edu.ucne.TicTacToePlay.domain.repository

import edu.ucne.TicTacToePlay.domain.model.Partida
import kotlinx.coroutines.flow.Flow

interface PartidaRepository {

    fun observePartidas(): Flow<List<Partida>>

    suspend fun getPartida(id: Int): Partida?

    suspend fun upsert(partida: Partida): Int

    suspend fun delete(id: Int)
}