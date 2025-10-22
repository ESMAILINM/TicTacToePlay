package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import javax.inject.Inject

class DeletePartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {

    suspend operator fun invoke(id: Int): Result<Unit> = runCatching {
        repository.delete(id)
    }
}