package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {

    operator fun invoke(): Flow<List<Partida>> {
        return repository.observePartidas()
    }
}