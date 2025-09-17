package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import javax.inject.Inject

class GetPartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {

    suspend operator fun invoke(id: Int): Result<Partida?> {
        return runCatching {
            repository.getPartida(id)
        }
    }
}
