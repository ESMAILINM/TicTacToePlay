package edu.ucne.TicTacToePlay.tareas.remote

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val jugadorRepository: JugadorRepository
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            when (jugadorRepository.postPendingJugadores()) {
                is Resource.Error -> return Result.retry()
                else -> {}
            }
            when (jugadorRepository.getJugadoresFromApi()) {
                is Resource.Success -> Result.success()
                is Resource.Error -> Result.retry()
                else -> Result.failure()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
