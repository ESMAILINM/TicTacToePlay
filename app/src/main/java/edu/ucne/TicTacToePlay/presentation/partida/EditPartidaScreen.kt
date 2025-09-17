package edu.ucne.TicTacToePlay.presentation.partida

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.ucne.TicTacToePlay.presentation.partidas.EditPartidaViewModel

@Composable
fun EditPartidaScreen(
    navController: NavController,
    partidaId: Int?,
    viewModel: EditPartidaViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    // Cargar partida si tiene ID
    LaunchedEffect(partidaId) {
        partidaId?.let { viewModel.onEvent(EditPartidaUiEvent.Load(it)) }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Fecha
        OutlinedTextField(
            value = state.fecha,
            onValueChange = { viewModel.onEvent(EditPartidaUiEvent.FechaChanged(it)) },
            label = { Text("Fecha") },
            isError = state.fechaError != null,
            modifier = Modifier.fillMaxWidth()
        )
        state.fechaError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

        // Jugador 1
        OutlinedTextField(
            value = state.jugador1Id.toString(),
            onValueChange = { viewModel.onEvent(EditPartidaUiEvent.Jugador1Changed(it.toIntOrNull() ?: 0)) },
            label = { Text("Jugador 1 ID") },
            isError = state.jugador1Error != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        state.jugador1Error?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

        // Jugador 2
        OutlinedTextField(
            value = state.jugador2Id.toString(),
            onValueChange = { viewModel.onEvent(EditPartidaUiEvent.Jugador2Changed(it.toIntOrNull() ?: 0)) },
            label = { Text("Jugador 2 ID") },
            isError = state.jugador2Error != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        state.jugador2Error?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

        // Ganador (opcional)
        OutlinedTextField(
            value = state.ganadorId?.toString() ?: "",
            onValueChange = { viewModel.onEvent(EditPartidaUiEvent.GanadorChanged(it.toIntOrNull())) },
            label = { Text("Ganador ID (opcional)") },
            isError = state.ganadorError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        state.ganadorError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

        // Es finalizada
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(
                checked = state.esFinalizada,
                onCheckedChange = { viewModel.onEvent(EditPartidaUiEvent.EsFinalizadaChanged(it)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Finalizada")
        }

        // Guardar y eliminar
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { viewModel.onEvent(EditPartidaUiEvent.Save) },
                enabled = !state.isSaving
            ) {
                Text(if (state.isNew) "Crear" else "Guardar")
            }

            if (!state.isNew) {
                Button(
                    onClick = { viewModel.onEvent(EditPartidaUiEvent.Delete) },
                    enabled = !state.isDeleting,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            }
        }

        // Mensajes de operación
        if (state.saved) Text("Partida guardada correctamente!", color = MaterialTheme.colorScheme.primary)
        if (state.deleted) Text("Partida eliminada", color = MaterialTheme.colorScheme.error)
    }
}
