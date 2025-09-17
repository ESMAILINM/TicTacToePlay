package edu.ucne.TicTacToePlay.presentation.partida.list

import edu.ucne.TicTacToePlay.domain.model.Partida

sealed class ListPartidaUiEvent {
    data class OnDeletePartidaClick(val partida: Partida) : ListPartidaUiEvent()
}