package edu.ucne.TicTacToePlay.domain.usecase.usecaseapi

import edu.ucne.TicTacToePlay.tareas.remote.TicTacToePlayApi
import edu.ucne.TicTacToePlay.tareas.remote.dto.PartidaDto
import javax.inject.Inject


class PostPartidaUseCase @Inject constructor(
    private val api: TicTacToePlayApi
) {
    suspend operator fun invoke(jugador1Id: Int, jugador2Id: Int = 2): Int? {
        val response = api.postPartida(PartidaDto(jugador1Id = jugador1Id, jugador2Id = jugador2Id))
        return if (response.isSuccessful) response.body()?.partidaId else null
    }
}