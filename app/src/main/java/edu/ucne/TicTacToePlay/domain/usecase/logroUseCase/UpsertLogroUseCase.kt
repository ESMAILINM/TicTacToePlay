package edu.ucne.TicTacToePlay.domain.usecase.logroUseCase

import edu.ucne.TicTacToePlay.domain.model.Logro
import edu.ucne.TicTacToePlay.domain.repository.LogroRepository
import javax.inject.Inject

class UpsertLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke(logro: Logro): Result<Int> {
        // Validación simple: la descripción no puede estar vacía
        if (logro.descripcion.isBlank()) {
            return Result.failure(IllegalArgumentException("La descripción no puede estar vacía"))
        }
        if (logro.nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío"))
        }

        // Ejecutar upsert y capturar errores
        return runCatching {
            repository.upsertLogro(logro)
        }
    }
}
