package edu.ucne.TicTacToePlay.presentation.logro.edit

data class EditLogroUiState(
    val logroId: Int? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val nombreError: String? = null,
    val descripcionError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val canBeDeleted: Boolean = false
)
