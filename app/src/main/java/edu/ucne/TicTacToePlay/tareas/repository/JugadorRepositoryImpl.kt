package edu.ucne.TicTacToePlay.tareas.repository

import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.tareas.local.dao.JugadorDao
import edu.ucne.TicTacToePlay.tareas.mapper.toDomain
import edu.ucne.TicTacToePlay.tareas.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JugadorRepositoryImpl @Inject constructor(private val dao: JugadorDao): JugadorRepository {

    override fun observeJugador(): Flow<List<Jugador>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun getJugador(id: Int): Jugador? = dao.getById(id)?.toDomain()

    override suspend fun upsertJugador(jugador: Jugador): Int {
        dao.upsert(jugador.toEntity())
        return jugador.jugadorId
    }

    override suspend fun deleteJugador(id: Int) = dao.delete(id)

    override suspend fun getJugadorByName(name: String): Jugador? {
        return dao.getJugadorByName(name)
    }

}