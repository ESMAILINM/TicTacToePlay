package edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.TicTacToePlay.tareas.remote.SyncWorker
import javax.inject.Inject

class TriggerSyncUseCase @Inject constructor(
    @ApplicationContext private val context: Context
){
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}