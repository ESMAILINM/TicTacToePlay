package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import edu.ucne.TicTacToePlay.domain.validation.PartidaValidator
import javax.inject.Inject

class UpsertPartidaUseCase @Inject constructor(
    private val repository: PartidaRepository,
    private val validator: PartidaValidator
) {

    suspend operator fun invoke(partida: Partida): Result<Int> {
        val jugadoresValidation = validator.validateJugadores(partida.jugador1Id, partida.jugador2Id)
        if (!jugadoresValidation.isValid) {
            return Result.failure(IllegalArgumentException(jugadoresValidation.error))
        }

        val ganadorValidation = validator.validateGanador(partida.ganadorId, partida.jugador1Id, partida.jugador2Id)
        if (!ganadorValidation.isValid) {
            return Result.failure(IllegalArgumentException(ganadorValidation.error))
        }

        val fechaValidation = validator.validateFecha(partida.fecha)
        if (!fechaValidation.isValid) {
            return Result.failure(IllegalArgumentException(fechaValidation.error))
        }
        
        return runCatching {
            repository.upsert(partida)
        }
    }
}
