package edu.ucne.TicTacToePlay.presentation.partida

sealed class EditPartidaUiEvent {
    data class Load(val partidaId: Int) : EditPartidaUiEvent()
    data class FechaChanged(val fecha: String) : EditPartidaUiEvent()
    data class Jugador1Changed(val jugadorId: Int) : EditPartidaUiEvent()
    data class Jugador2Changed(val jugadorId: Int) : EditPartidaUiEvent()
    data class GanadorChanged(val ganadorId: Int?) : EditPartidaUiEvent()
    data class EsFinalizadaChanged(val finalizada: Boolean) : EditPartidaUiEvent()
    object Save : EditPartidaUiEvent()
    object Delete : EditPartidaUiEvent()
}
