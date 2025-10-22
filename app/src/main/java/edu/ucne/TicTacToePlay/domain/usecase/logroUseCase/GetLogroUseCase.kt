package edu.ucne.TicTacToePlay.domain.usecase.logroUseCase

import edu.ucne.TicTacToePlay.domain.model.Logro
import edu.ucne.TicTacToePlay.domain.repository.LogroRepository
import javax.inject.Inject

class GetLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke(id: Int): Logro? {
        return repository.getLogro(id)
    }
}
