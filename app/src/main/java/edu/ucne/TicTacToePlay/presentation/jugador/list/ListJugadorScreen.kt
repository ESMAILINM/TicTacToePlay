package edu.ucne.TicTacToePlay.presentation.jugador.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.TicTacToePlay.domain.model.Jugador

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListJugadorScreen(
    navController: NavController,
    viewModel: JugadorListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jugadores") },
                actions = {
                    IconButton(onClick = { navController.navigate("list_partida_screen") }) {
                        Icon(Icons.Default.List, contentDescription = "Ver partidas")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("edit_jugador_screen/0") },
                containerColor = Color.Gray
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
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.jugadores.isEmpty()) {
                Text("No hay jugadores. ¡Añade uno!", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.jugadores, key = { it.jugadorId }) { jugador ->
                        JugadorItem(
                            jugador = jugador,
                            onJugadorClick = {
                                navController.navigate("edit_jugador_screen/${jugador.jugadorId}")
                            },
                            onDeleteClick = {
                                viewModel.onEvent(ListJugadorUiEvent.OnDeleteJugadorClick(jugador))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JugadorItem(
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
                Text("Partidas: ${jugador.partidas}")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar jugador", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
