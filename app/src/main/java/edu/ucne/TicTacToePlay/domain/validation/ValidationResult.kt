package edu.ucne.TicTacToePlay.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)
