package edu.ucne.TicTacToePlay.presentation.jugador.list

import edu.ucne.TicTacToePlay.domain.model.Jugador

    interface ListJugadorUiEvent {
        data class OnDeleteJugadorClick(val jugador: Jugador) : ListJugadorUiEvent
    }
