package edu.ucne.TicTacToePlay.domain.usecase.logroUseCase

import edu.ucne.TicTacToePlay.domain.repository.LogroRepository
import javax.inject.Inject

class DeleteLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteLogro(id)
}
