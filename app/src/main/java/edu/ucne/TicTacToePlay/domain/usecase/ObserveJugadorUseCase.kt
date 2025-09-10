package edu.ucne.TicTacToePlay.domain.usecase

import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    operator fun invoke(): Flow<List<Jugador>> {
        return repository.observeJugador()
    }


}