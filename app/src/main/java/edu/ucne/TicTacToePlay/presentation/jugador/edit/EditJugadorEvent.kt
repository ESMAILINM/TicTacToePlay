package edu.ucne.TicTacToePlay.presentation.jugador.edit

interface EditJugadorEvent {
    data class Load(val id: Int?): EditJugadorEvent
    data class NombreChanged(val nombre: String): EditJugadorEvent
    data class PartidasChanged(val partidas: String): EditJugadorEvent
    data object Save: EditJugadorEvent
    data object Delete: EditJugadorEvent
}