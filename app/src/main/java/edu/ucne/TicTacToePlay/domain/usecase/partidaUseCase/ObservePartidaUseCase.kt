package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow

class ObservePartidaUseCase(
    private val repository: PartidaRepository
) {

    operator fun invoke(): Flow<List<Partida>> {
        return repository.observePartidas()
    }
}