package edu.ucne.TicTacToePlay.presentation.logro.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.TicTacToePlay.domain.model.Logro
import edu.ucne.TicTacToePlay.domain.usecase.logroUseCase.DeleteLogroUseCase
import edu.ucne.TicTacToePlay.domain.usecase.logroUseCase.GetLogroUseCase
import edu.ucne.TicTacToePlay.domain.usecase.logroUseCase.UpsertLogroUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditLogroViewModel @Inject constructor(
    private val upsertLogroUseCase: UpsertLogroUseCase,
    private val getLogroUseCase: GetLogroUseCase,
    private val deleteLogroUseCase: DeleteLogroUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditLogroUiState())
    val state: StateFlow<EditLogroUiState> = _state.asStateFlow()

    fun onEvent(event: EditLogroUiEvent) {
        when (event) {
            is EditLogroUiEvent.Load -> onLoad(event.id)
            is EditLogroUiEvent.NombreChanged -> _state.update {
                it.copy(nombre = event.nombre, nombreError = null)
            }
            is EditLogroUiEvent.DescripcionChanged -> _state.update {
                it.copy(descripcion = event.descripcion, descripcionError = null)
            }
            EditLogroUiEvent.Save -> onSave()
            EditLogroUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update {
                it.copy(
                    isLoading = false,
                    logroId = null,
                    nombre = "",
                    descripcion = "",
                    errorMessage = null,
                    isSaved = false,
                    isSaving = false,
                    isDeleting = false,
                    canBeDeleted = false
                )
            }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val logro = getLogroUseCase(id)
                _state.update {
                    if (logro != null) {
                        it.copy(
                            logroId = logro.logroId,
                            nombre = logro.nombre,
                            descripcion = logro.descripcion,
                            isLoading = false,
                            errorMessage = null,
                            canBeDeleted = true
                        )
                    } else {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Logro con ID $id no encontrado.",
                            logroId = null,
                            nombre = "",
                            descripcion = "",
                            canBeDeleted = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido al cargar el logro.",
                        canBeDeleted = false
                    )
                }
            }
        }
    }

    private fun onSave() {
        val nombre = state.value.nombre
        val descripcion = state.value.descripcion
        val currentLogroId = state.value.logroId

        // Validaciones simples
        val nombreError = if (nombre.isBlank()) "El nombre no puede estar vacío" else null
        val descripcionError = if (descripcion.isBlank()) "La descripción no puede estar vacía" else null

        if (nombreError != null || descripcionError != null) {
            _state.update {
                it.copy(
                    nombreError = nombreError,
                    descripcionError = descripcionError,
                    errorMessage = "Por favor, corrija los errores en el formulario."
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }

            val logro = Logro(
                logroId = currentLogroId ?: 0,
                nombre = nombre,
                descripcion = descripcion
            )

            val result = upsertLogroUseCase(logro)
            result.onSuccess { newId ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        isSaved = true,
                        logroId = newId,
                        errorMessage = null
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Error desconocido al guardar el logro."
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val logroId = _state.value.logroId
        if (logroId == null || logroId == 0) {
            _state.update { it.copy(errorMessage = "No se puede eliminar un Logro sin ID válido.") }
            return
        }

        _state.update { it.copy(isDeleting = true, isSaved = false, errorMessage = null) }

        viewModelScope.launch {
            try {
                deleteLogroUseCase(logroId)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        isSaved = true,
                        errorMessage = null,
                        logroId = null,
                        nombre = "",
                        descripcion = "",
                        canBeDeleted = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = e.message ?: "Error desconocido al eliminar el logro."
                    )
                }
            }
        }
    }
}
