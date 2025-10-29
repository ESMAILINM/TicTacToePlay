package edu.ucne.TicTacToePlay.presentation.jugador.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListJugadorScreen(
    navController: NavController,
    viewModel: JugadorListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ListJugadorScreenBody(
        state = state,
        onEvent = viewModel::onEvent,
        onJugadorClick = { jugador ->
            navController.navigate(Screen.Jugador.createRoute(jugador.jugadorId))
        },
        onAddClick = {
            navController.navigate(Screen.Jugador.createRoute("0"))
        },
        onSync = { viewModel.startSync() }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListJugadorScreenBody(
    state: ListJugadorUiState,
    onEvent: (ListJugadorUiEvent) -> Unit,
    onJugadorClick: (Jugador) -> Unit,
    onAddClick: () -> Unit,
    onSync: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(ListJugadorUiEvent.ShowMessage(""))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(onClick = onSync) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Sincronizar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir jugador")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.jugadores.isEmpty() -> {
                    Text(
                        "No hay jugadores. ¡Añade uno!",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.jugadores, key = { it.nombres }) { jugador ->
                            JugadorItem(
                                jugador = jugador,
                                onJugadorClick = { onJugadorClick(jugador) },
                                onDeleteClick = { onEvent(ListJugadorUiEvent.OnDeleteJugadorClick(jugador)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JugadorItem(
    jugador: Jugador,
    onJugadorClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onJugadorClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(jugador.nombres, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Email: ${jugador.email}", style = MaterialTheme.typography.bodyMedium)
                Text("ReomoteId: ${jugador.remoteId}", style = MaterialTheme.typography.bodyMedium)
                Text("LocalId: ${jugador.jugadorId}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar jugador",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 700)
@Composable
fun ListJugadorScreenPreview() {
    val jugadores = listOf(
        Jugador(nombres = "Juan Pérez", email = "", remoteId = 1, jugadorId = "1"),
        Jugador(nombres = "Ana Gómez", email = "", remoteId = 2, jugadorId = "2"),
    )
    ListJugadorScreenBody(
        state = ListJugadorUiState(jugadores = jugadores, isLoading = false),
        onEvent = {},
        onJugadorClick = {},
        onAddClick = {},
        onSync = {}
    )
}

