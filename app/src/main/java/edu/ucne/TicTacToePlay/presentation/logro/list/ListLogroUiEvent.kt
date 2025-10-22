package edu.ucne.TicTacToePlay.presentation.logro.list

import edu.ucne.TicTacToePlay.domain.model.Logro

interface ListLogroUiEvent {
    data class OnDeleteLogroClick(val logro: Logro) : ListLogroUiEvent
}
