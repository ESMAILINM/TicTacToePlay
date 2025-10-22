package edu.ucne.TicTacToePlay.presentation.partida.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.presentation.jugador.list.JugadorListViewModel
import edu.ucne.TicTacToePlay.ui.theme.TicTacToePlayTheme
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPartidaScreen(
    navController: NavController,
    partidaViewModel: ListPartidaViewModel = hiltViewModel(),
    jugadorViewModel: JugadorListViewModel = hiltViewModel()
) {
    val partidasState by partidaViewModel.state.collectAsStateWithLifecycle()
    val jugadoresState by jugadorViewModel.state.collectAsStateWithLifecycle()

    val jugadorMap = remember(jugadoresState.jugadores) {
        jugadoresState.jugadores.associateBy({ it.jugadorId }, { it.nombres })
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("tictactoe_screen/0") },
                containerColor = MaterialTheme.colorScheme.primary
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
            when {
                partidasState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                partidasState.partidas.isEmpty() -> {
                    Text(
                        "No hay partidas. ¡Añade una!",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(partidasState.partidas, key = { it.partidaId }) { partida ->
                            PartidaItem(
                                partida = partida,
                                jugadorMap = jugadorMap,
                                onPartidaClick = {
                                    navController.navigate("edit_partida_screen/${partida.partidaId}")
                                },
                                onDeleteClick = {
                                    partidaViewModel.onEvent(ListPartidaUiEvent.OnDeletePartidaClick(partida))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PartidaItem(
    partida: Partida,
    jugadorMap: Map<Int, String>,
    onPartidaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onPartidaClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Botón eliminar
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar partida",
                    tint = MaterialTheme.colorScheme.error
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Partida #${partida.partidaId}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Fecha: ${partida.fecha}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Jugador 1:", style = MaterialTheme.typography.labelMedium)
                        Text(jugadorMap[partida.jugador1Id] ?: "N/A", style = MaterialTheme.typography.bodyMedium)
                    }
                    Column {
                        Text("Jugador 2:", style = MaterialTheme.typography.labelMedium)
                        Text(jugadorMap[partida.jugador2Id] ?: "N/A", style = MaterialTheme.typography.bodyMedium)
                    }
                    Column {
                        Text("Ganador:", style = MaterialTheme.typography.labelMedium)
                        Text(jugadorMap[partida.ganadorId ?: -1] ?: "N/A", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Finalizada: ${if (partida.esFinalizada) "Sí" else "No"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (partida.esFinalizada)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
@Preview(showBackground = true, name = "Partida Item Preview")
@Composable
private fun PartidaItemPreview() {
    val partidaDeEjemplo = Partida(
        partidaId = 101,
        fecha = LocalDate.now().toString(),
        jugador1Id = 1,
        jugador2Id = 2,
        ganadorId = 1,
        esFinalizada = true
    )

    val mapaDeJugadores = mapOf(
        1 to "Esmailin",
        2 to "Ana"
    )
    TicTacToePlayTheme {
        Surface {
            PartidaItem(
                partida = partidaDeEjemplo,
                jugadorMap = mapaDeJugadores,
                onPartidaClick = {},
                onDeleteClick = {}
            )
        }
    }
}
