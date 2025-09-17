package edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase

import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.domain.validation.JugadorValidator
import javax.inject.Inject

class UpsertJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository,
    private val validator: JugadorValidator
) {
    suspend operator fun invoke(jugador: Jugador): Result<Int>{
        val nombreResult = validator.validateNombreUnico(jugador.nombres, jugador.jugadorId)
        if (!nombreResult.isValid) {
            return Result.failure(IllegalArgumentException(nombreResult.error))
        }
        val partidaResult = validator.validatePartida(jugador.partidas.toString())
        if((!partidaResult.isValid)){
            return Result.failure(IllegalArgumentException(partidaResult.error))
        }
        return runCatching { repository.upsertJugador(jugador) }
    }
}