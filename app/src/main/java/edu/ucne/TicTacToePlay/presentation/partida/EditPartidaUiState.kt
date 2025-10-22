package edu.ucne.TicTacToePlay.presentation.partida

import edu.ucne.TicTacToePlay.domain.model.Jugador

data class EditPartidaUiState(
    val partidaId: Int? = null,
    val fecha: String = "",
    val jugador1Id: Int = 0,
    val jugador2Id: Int = 0,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false,
    val fechaError: String? = null,
    val jugador1Error: String? = null,
    val jugador2Error: String? = null,
    val ganadorError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false,

    // Lista de jugadores disponibles para selector
    val jugadoresDisponibles: List<Jugador> = emptyList()
)
