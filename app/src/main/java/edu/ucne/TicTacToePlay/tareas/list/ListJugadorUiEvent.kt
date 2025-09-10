package edu.ucne.TicTacToePlay.tareas.list

import edu.ucne.TicTacToePlay.domain.model.Jugador

    interface ListJugadorUiEvent {
        data class OnDeleteJugadorClick(val jugador: Jugador) : ListJugadorUiEvent
    }
