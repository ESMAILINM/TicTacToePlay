package edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase

import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.tareas.remote.Resource
import javax.inject.Inject

class PostPendingJugadoresUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.postPendingJugadores()
    }
}
