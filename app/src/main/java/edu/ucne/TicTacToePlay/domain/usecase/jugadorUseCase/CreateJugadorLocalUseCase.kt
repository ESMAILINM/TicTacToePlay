package edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase

import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.tareas.remote.Resource
import javax.inject.Inject

class CreateJugadorLocalUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(jugador: Jugador): Resource<Jugador> = repository.createJugadorLocal(jugador)
}