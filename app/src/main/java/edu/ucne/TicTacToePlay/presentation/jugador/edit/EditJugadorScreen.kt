package edu.ucne.TicTacToePlay.presentation.jugador.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun EditJugadorScreen(
    navController: NavController,
    jugadorId: String?,
    viewModel: EditJugadorViewModel = hiltViewModel()
) {
    LaunchedEffect(jugadorId) {
        viewModel.onEvent(EditJugadorEvent.Load(jugadorId))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }

    EditJugadorBody(state = state, onEvent = viewModel::onEvent)
}

@Composable
private fun EditJugadorBody(
    state: EditJugadorUiState,
    onEvent: (EditJugadorEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                onEvent(EditJugadorEvent.UserMessageShown)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.nombres,
                onValueChange = { onEvent(EditJugadorEvent.NombresChanged(it)) },
                label = { Text("Nombre") },
                isError = state.nombreError != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            state.nombreError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = state.email,
                onValueChange = { onEvent(EditJugadorEvent.EmailChanged(it)) },
                label = { Text("Correo electrónico") },
                isError = state.emailError != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            state.emailError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onEvent(EditJugadorEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Guardar")
                    }
                }

                if (state.canBeDeleted) {
                    OutlinedButton(
                        onClick = { state.jugadorId?.let { id -> onEvent(EditJugadorEvent.Delete(id)) } },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        if (state.isDeleting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}
