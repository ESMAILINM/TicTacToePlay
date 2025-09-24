package edu.ucne.TicTacToePlay.presentation.logro.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun EditLogroScreen(
    navController: NavController,
    logroId: Int?,
    viewModel: EditLogroViewModel = hiltViewModel()
) {
    LaunchedEffect(logroId) {
        viewModel.onEvent(EditLogroUiEvent.Load(logroId))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }
    EditLogroBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun EditLogroBody(
    state: EditLogroUiState,
    onEvent: (EditLogroUiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(EditLogroUiEvent.NombreChanged(it)) },
                label = { Text("Nombre") },
                isError = state.nombreError != null,
                supportingText = {
                    state.nombreError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { onEvent(EditLogroUiEvent.DescripcionChanged(it)) },
                label = { Text("Descripción") },
                isError = state.descripcionError != null,
                supportingText = {
                    state.descripcionError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onEvent(EditLogroUiEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC0CB)
                    )
                ) {
                    Text("Guardar")
                }

                if (state.canBeDeleted) {
                    OutlinedButton(
                        onClick = { onEvent(EditLogroUiEvent.Delete) },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

class EditLogroUiStatePreviewProvider : PreviewParameterProvider<EditLogroUiState> {
    override val values = sequenceOf(
        EditLogroUiState(
            nombre = "",
            descripcion = "",
            canBeDeleted = false
        ),
        EditLogroUiState(
            logroId = 1,
            nombre = "Primer triunfo",
            descripcion = "Ganar la primera partida",
            canBeDeleted = true
        ),
        EditLogroUiState(
            nombre = "ir",
            descripcion = "",
            nombreError = "El nombre es muy corto.",
            descripcionError = "La descripción no puede estar vacía.",
            canBeDeleted = false
        ),
        EditLogroUiState(
            logroId = 1,
            nombre = "Campeón",
            descripcion = "Ganar 10 partidas seguidas",
            isSaving = true,
            canBeDeleted = true
        ),
        EditLogroUiState(
            logroId = 1,
            nombre = "Eliminar logro",
            descripcion = "Logro marcado para eliminación",
            isDeleting = true,
            canBeDeleted = true
        )
    )
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun EditLogroBodyPreview(
    @PreviewParameter(EditLogroUiStatePreviewProvider::class) state: EditLogroUiState
) {
    MaterialTheme {
        EditLogroBody(
            state = state,
            onEvent = { event ->
                println("Preview Event: $event")
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun EditLogroBodyNewLogroPreview() {
    MaterialTheme {
        EditLogroBody(
            state = EditLogroUiState(
                nombre = "",
                descripcion = "",
                canBeDeleted = false,
                isSaving = false,
                isDeleting = false,
                nombreError = null,
                descripcionError = null
            ),
            onEvent = {}
        )
    }
}
