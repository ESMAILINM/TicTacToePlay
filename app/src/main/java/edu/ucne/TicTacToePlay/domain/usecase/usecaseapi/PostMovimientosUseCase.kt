package edu.ucne.TicTacToePlay.domain.usecase.usecaseapi

import edu.ucne.TicTacToePlay.tareas.remote.TicTacToePlayApi
import edu.ucne.TicTacToePlay.tareas.remote.dto.MovimientoDto
import javax.inject.Inject

class PostMovimientoUseCase @Inject constructor(
    private val api: TicTacToePlayApi
) {
    suspend operator fun invoke(movimiento: MovimientoDto): Boolean {
        val response = api.postMovimiento(movimiento)
        return response.isSuccessful
    }
}