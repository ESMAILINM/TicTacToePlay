package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository

class UpsertPartidaUseCase(
    private val repository: PartidaRepository
) {

    suspend operator fun invoke(partida: Partida): Result<Int> {

        if (partida.jugador1Id == partida.jugador2Id) {
            return Result.failure(
                IllegalArgumentException("Jugador 1 y Jugador 2 no pueden ser el mismo")
            )
        }

        return runCatching {
            repository.upsert(partida)
        }
    }
}