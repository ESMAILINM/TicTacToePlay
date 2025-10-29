package edu.ucne.TicTacToePlay.domain.repository

import android.content.res.Resources
import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.tareas.local.entities.JugadorEntity
import edu.ucne.TicTacToePlay.tareas.remote.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.Response



interface JugadorRepository {
    fun observeJugador(): Flow<List<Jugador>>
    suspend fun getJugador(id: String): Jugador?
    suspend fun upsertJugador(jugador: Jugador): String
    suspend fun deleteJugador(id: String) : Resource<Unit>
    suspend fun getJugadorByName(name: String): Jugador?
    suspend fun createJugadorLocal(jugador: Jugador): Resource<Jugador>
    suspend fun postPendingJugadores(): Resource<Unit>
    suspend fun getJugadoresPendingCreate(): List<Jugador>
     suspend fun getJugadoresFromApi(): Resource<Unit>
}