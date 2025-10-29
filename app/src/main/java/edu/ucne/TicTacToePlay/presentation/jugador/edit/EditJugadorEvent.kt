package edu.ucne.TicTacToePlay.presentation.jugador.edit

import edu.ucne.TicTacToePlay.domain.model.Jugador

sealed interface EditJugadorEvent {
    data class Load(val id: String?) : EditJugadorEvent
    data class NombresChanged(val nombres: String) : EditJugadorEvent
    data class EmailChanged(val email: String) : EditJugadorEvent
    object Save : EditJugadorEvent
    data class Delete(val id: String) : EditJugadorEvent
    data class CrearJugador(val nombres: String, val partidas: Int) : EditJugadorEvent
    data class UpdateJugador(val jugador: Jugador) : EditJugadorEvent
    object ShowCreateSheet : EditJugadorEvent
    object HideCreateSheet : EditJugadorEvent
    object UserMessageShown : EditJugadorEvent
}
