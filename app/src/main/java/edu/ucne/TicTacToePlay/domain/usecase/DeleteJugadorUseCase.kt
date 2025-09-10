package edu.ucne.TicTacToePlay.domain.usecase

import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import javax.inject.Inject

class DeleteJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteJugador(id)
}