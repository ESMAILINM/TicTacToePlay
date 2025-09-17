package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository

class GetPartidaUseCase(
    private val repository: PartidaRepository
) {

    suspend operator fun invoke(id: Int): Result<Partida?> {
        return runCatching {
            repository.getPartida(id)
        }
    }
}
