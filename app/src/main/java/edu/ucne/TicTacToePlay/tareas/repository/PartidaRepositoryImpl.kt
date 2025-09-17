package edu.ucne.TicTacToePlay.data.repository

import android.R
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import edu.ucne.TicTacToePlay.tareas.local.dao.PartidaDao
import edu.ucne.TicTacToePlay.tareas.local.entities.PartidaEntity
import edu.ucne.TicTacToePlay.tareas.mapper.toDomain
import edu.ucne.TicTacToePlay.tareas.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartidaRepositoryImpl @Inject constructor(
    private val dao: PartidaDao
) : PartidaRepository {

    override fun observePartidas(): Flow<List<Partida>> =
        dao.observeAll().map { list: List<PartidaEntity> ->
            list.map { it.toDomain() }
        }

    override suspend fun getPartida(id: Int): Partida? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(partida: Partida): Int {
        val id = dao.upsert(partida.toEntity())
        return if (partida.partidaId == 0) id.toInt() else partida.partidaId
    }


    override suspend fun delete(id: Int) {
        dao.deleteById(id)
    }
}
