package edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase

import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.tareas.remote.Resource
import javax.inject.Inject

class DeleteJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(id: String): Resource<Unit> = repository.deleteJugador(id)
}