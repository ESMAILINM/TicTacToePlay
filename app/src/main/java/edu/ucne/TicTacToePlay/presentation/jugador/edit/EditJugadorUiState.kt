package edu.ucne.TicTacToePlay.presentation.jugador.edit

import edu.ucne.TicTacToePlay.domain.model.Jugador

data class EditJugadorUiState(
    val jugadorId: String? = null,
    val jugador: List<Jugador> = emptyList(),
    val nombres: String = "",
    val email: String = "",
    val nombreError: String? = null,
    val emailError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val canBeDeleted: Boolean = false,
    val userMessage: String? = null,
    val showCreateSheet: Boolean = false
    )

