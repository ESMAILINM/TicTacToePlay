package edu.ucne.TicTacToePlay.tareas.remote

import edu.ucne.TicTacToePlay.presentation.tictactoe.GameUiState
import edu.ucne.TicTacToePlay.tareas.remote.dto.MovimientoDto

fun List<MovimientoDto>.toDomainList(): List<MovimientoDto> {
    return this.map {
        MovimientoDto(
            partidaId = it.partidaId,
            jugador = it.jugador,
            posicionFila = it.posicionFila,
            posicionColumna = it.posicionColumna
        )
    }
}

fun GameUiState.toDtoList(partidaId: Int): List<MovimientoDto> {
    val movimientos = mutableListOf<MovimientoDto>()
    board.forEachIndexed { index, player ->
        if (player != null) {
            val fila = index / 3 + 1
            val columna = index % 3 + 1
            movimientos.add(
                MovimientoDto(
                    jugador = player.symbol,
                    posicionFila = fila,
                    posicionColumna = columna,
                    partidaId = partidaId
                )
            )
        }
    }
    return movimientos
}