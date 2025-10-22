package edu.ucne.TicTacToePlay.domain.usecase.logroUseCase

import edu.ucne.TicTacToePlay.domain.model.Logro
import edu.ucne.TicTacToePlay.domain.repository.LogroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    operator fun invoke(): Flow<List<Logro>> {
        return repository.observeLogros()
    }
}
