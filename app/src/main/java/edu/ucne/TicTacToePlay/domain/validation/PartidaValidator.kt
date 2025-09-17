package edu.ucne.TicTacToePlay.domain.validation

import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import edu.ucne.TicTacToePlay.domain.validation.ValidationResult
import javax.inject.Inject

class PartidaValidator @Inject constructor(
    private val repository: PartidaRepository
) {

    fun validateJugadores(jugador1Id: Int, jugador2Id: Int): ValidationResult {
        return if (jugador1Id == jugador2Id) {
            ValidationResult(false, "Jugador 1 y Jugador 2 no pueden ser el mismo")
        } else {
            ValidationResult(true)
        }
    }

    fun validateGanador(ganadorId: Int?, jugador1Id: Int, jugador2Id: Int): ValidationResult {
        return if (ganadorId != null && ganadorId != jugador1Id && ganadorId != jugador2Id) {
            ValidationResult(false, "El ganador debe ser Jugador 1 o Jugador 2")
        } else {
            ValidationResult(true)
        }
    }
    fun validateFecha(fecha: String): ValidationResult {
        return if (fecha.isBlank()) {
            ValidationResult(false, "La fecha no puede estar vacía")
        } else {
            ValidationResult(true) } }
}
