package edu.ucne.TicTacToePlay.domain.usecase.usecaseapi


import edu.ucne.TicTacToePlay.tareas.remote.TicTacToePlayApi
import edu.ucne.TicTacToePlay.tareas.remote.dto.MovimientoDto
import javax.inject.Inject

class GetPartidaApiUseCase @Inject constructor(
    private val api: TicTacToePlayApi
) {
    suspend operator fun invoke(partidaId: Int): List<MovimientoDto> = try {
        api.getMovimientos(partidaId)
    } catch (e: Exception) {
        emptyList()
    }
}

