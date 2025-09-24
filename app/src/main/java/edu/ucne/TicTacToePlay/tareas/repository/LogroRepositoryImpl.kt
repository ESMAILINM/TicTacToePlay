package edu.ucne.TicTacToePlay.tareas.repository

import edu.ucne.TicTacToePlay.domain.model.Logro
import edu.ucne.TicTacToePlay.domain.repository.LogroRepository
import edu.ucne.TicTacToePlay.tareas.local.dao.LogroDao
import edu.ucne.TicTacToePlay.tareas.mapper.toDomain
import edu.ucne.TicTacToePlay.tareas.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LogroRepositoryImpl @Inject constructor(
    private val dao: LogroDao
): LogroRepository {
    override fun observeLogros(): Flow<List<Logro>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
}
    override suspend fun  getLogro(id: Int): Logro? = dao.getById(id)?.toDomain()

    override suspend fun upsertLogro(logro: Logro): Int {
        dao.upsert(logro.toEntity())
        return logro.logroId
    }
    override suspend fun deleteLogro(id: Int) {
        dao.deleteById(id)
    }
}
