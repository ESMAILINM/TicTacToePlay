package edu.ucne.TicTacToePlay.presentation.logro.edit

interface EditLogroUiEvent {
    data class Load(val id: Int?) : EditLogroUiEvent
    data class NombreChanged(val nombre: String) : EditLogroUiEvent
    data class DescripcionChanged(val descripcion: String) : EditLogroUiEvent
    object Save : EditLogroUiEvent
    object Delete : EditLogroUiEvent


}