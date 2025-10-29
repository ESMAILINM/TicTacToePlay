package edu.ucne.TicTacToePlay.domain.repository

import edu.ucne.TicTacToePlay.domain.model.Jugador
import kotlinx.coroutines.flow.Flow

interface JugadorRepository {
    fun observeJugador(): Flow<List<Jugador>>
    suspend fun getJugador(id: Int): Jugador?
    suspend fun upsertJugador(jugador: Jugador): Int
    suspend fun deleteJugador(id: Int)

    suspend fun getJugadorByName(name: String): Jugador?
}