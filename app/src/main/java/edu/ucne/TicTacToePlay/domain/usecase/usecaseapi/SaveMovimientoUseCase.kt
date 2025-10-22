package edu.ucne.TicTacToePlay.domain.usecase.usecaseapi
import edu.ucne.TicTacToePlay.tareas.remote.TicTacToePlayApi
import edu.ucne.TicTacToePlay.tareas.remote.dto.MovimientoDto
import javax.inject.Inject

class SaveMovimientosUseCase @Inject constructor(
    private val api: TicTacToePlayApi
) {
    suspend operator fun invoke(partidaId: Int, movimientos: List<MovimientoDto>) {
        movimientos.forEach { mov ->
            api.postMovimiento(mov)
        }
    }
}
