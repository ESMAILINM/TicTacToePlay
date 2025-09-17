package edu.ucne.TicTacToePlay.presentation.partida.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
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
import edu.ucne.TicTacToePlay.domain.model.Partida

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPartidaScreen(
    navController: NavController,
    viewModel: ListPartidaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Partidas") },
                actions = {
                    IconButton(onClick = { navController.navigate("list_jugador_screen") }) {
                        Icon(Icons.Default.Person, contentDescription = "Ver jugadores")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("edit_partida_screen/0") },
                containerColor = Color.Gray
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir partida")
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
            } else if (state.partidas.isEmpty()) {
                Text("No hay partidas. ¡Añade una!", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.partidas, key = { it.partidaId }) { partida ->
                        PartidaItem(
                            partida = partida,
                            onPartidaClick = {
                                navController.navigate("edit_partida_screen/${partida.partidaId}")
                            },
                            onDeleteClick = {
                                viewModel.onEvent(ListPartidaUiEvent.OnDeletePartidaClick(partida))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PartidaItem(
    partida: Partida,
    onPartidaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onPartidaClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Partida ID: ${partida.partidaId}", style = MaterialTheme.typography.bodyLarge)
            Text("Fecha: ${partida.fecha}", style = MaterialTheme.typography.bodyMedium)
            Text("Jugador 1: ${partida.jugador1Id} | Jugador 2: ${partida.jugador2Id}", style = MaterialTheme.typography.bodyMedium)
            Text("Ganador: ${partida.ganadorId ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text("Finalizada: ${if (partida.esFinalizada) "Sí" else "No"}", style = MaterialTheme.typography.bodyMedium)

            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar partida", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
