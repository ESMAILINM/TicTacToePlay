package edu.ucne.TicTacToePlay.tareas.repository

import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.tareas.local.dao.JugadorDao
import edu.ucne.TicTacToePlay.tareas.mapper.toDomain
import edu.ucne.TicTacToePlay.tareas.mapper.toEntity
import edu.ucne.TicTacToePlay.tareas.mapper.toRequest
import edu.ucne.TicTacToePlay.tareas.remote.JugadorRemoteDataSource
import edu.ucne.TicTacToePlay.tareas.remote.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JugadorRepositoryImpl @Inject constructor(
    private val localDataSource: JugadorDao,
    private val remoteDataSource: JugadorRemoteDataSource
) : JugadorRepository {

    override fun observeJugador(): Flow<List<Jugador>> =
        localDataSource.observeAll().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getJugador(id: String): Jugador? =
        localDataSource.getById(id)?.toDomain()

    override suspend fun createJugadorLocal(jugador: Jugador): Resource<Jugador> {
        val pending = jugador.copy(isPendingCreate = true)
        localDataSource.upsert(pending.toEntity())
        return Resource.Success(pending)
    }

    override suspend fun upsertJugador(jugador: Jugador): String {
        val remoteId = jugador.remoteId
        val request = jugador.toRequest()

        if (remoteId == null) {
            val pending = jugador.copy(isPendingCreate = true)
            localDataSource.upsert(pending.toEntity())
            return pending.jugadorId
        }

        return when (val result = remoteDataSource.updateJugador(remoteId, request)) {
            is Resource.Success -> {
                localDataSource.upsert(jugador.toEntity())
                jugador.jugadorId
            }
            is Resource.Error -> {
                val pending = jugador.copy(isPendingCreate = true)
                localDataSource.upsert(pending.toEntity())
                pending.jugadorId
            }
            else -> {
                localDataSource.upsert(jugador.toEntity())
                jugador.jugadorId
            }
        }
    }

    override suspend fun deleteJugador(id: String): Resource<Unit> {
        val jugadorEntity = localDataSource.getById(id) ?: return Resource.Error("Jugador no encontrado")
        val remoteId = jugadorEntity.remoteId

        return try {
            if (remoteId != null) {
                when (val result = remoteDataSource.deleteJugador(remoteId)) {
                    is Resource.Success -> localDataSource.delete(id)
                    is Resource.Error -> return Resource.Error(result.message ?: "Error al eliminar jugador remoto")
                    else -> {}
                }
            } else {
                localDataSource.delete(id)
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error al eliminar jugador")
        }
    }

    override suspend fun postPendingJugadores(): Resource<Unit> {
        val pendingEntities = localDataSource.getJugadoresPendingCreate()
        var errorOccurred = false

        pendingEntities.forEach { entity ->
            val request = entity.toDomain().toRequest()
            try {
                when (val result = remoteDataSource.createJugador(request)) {
                    is Resource.Success -> {
                        val synced = entity.toDomain().copy(
                            remoteId = result.data?.jugadorId,
                            isPendingCreate = false
                        )
                        localDataSource.upsert(synced.toEntity())
                    }
                    is Resource.Error -> {
                        errorOccurred = true
                        println("Error sincronizando ${entity.nombres}: ${result.message}")
                    }
                    else -> { }
                }
            } catch (e: Exception) {
                errorOccurred = true
                println("Excepción sincronizando ${entity.nombres}: ${e.message}")
            }
        }

        return if (errorOccurred) Resource.Error("Algunos jugadores no se pudieron sincronizar")
        else Resource.Success(Unit)
    }


    override suspend fun getJugadoresFromApi(): Resource<Unit> {
        return when (val result = remoteDataSource.getJugadoresFromApi()) {
            is Resource.Success -> {
                result.data?.forEach { jugadorResponse ->
                    localDataSource.upsert(jugadorResponse.toDomain().toEntity())
                }
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al sincronizar jugadores")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getJugadorByName(name: String): Jugador? =
        localDataSource.getJugadorByName(name)?.toDomain()


    override suspend fun getJugadoresPendingCreate(): List<Jugador> =
        localDataSource.getJugadoresPendingCreate().map { it.toDomain() }
}
