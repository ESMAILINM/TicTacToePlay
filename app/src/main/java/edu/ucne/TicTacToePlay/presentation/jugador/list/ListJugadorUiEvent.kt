package edu.ucne.TicTacToePlay.presentation.jugador.list

import edu.ucne.TicTacToePlay.domain.model.Jugador

    sealed interface ListJugadorUiEvent {
        data class OnDeleteJugadorClick(val jugador: Jugador) : ListJugadorUiEvent
        data class Delete(val id: Int) : ListJugadorUiEvent
        data object Load : ListJugadorUiEvent
        data class ShowMessage(val message: String) : ListJugadorUiEvent

    }
