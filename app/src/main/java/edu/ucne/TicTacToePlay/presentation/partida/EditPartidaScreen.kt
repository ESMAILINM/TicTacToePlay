package edu.ucne.TicTacToePlay.presentation.partida

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.presentation.jugador.list.JugadorListViewModel
import edu.ucne.TicTacToePlay.utils.getFechaActual
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPartidaScreen(
    navController: NavController,
    partidaId: Int?,
    viewModel: EditPartidaViewModel = hiltViewModel(),
    jugadoresViewModel: JugadorListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val jugadoresState by jugadoresViewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var selectingJugador1 by remember { mutableStateOf(true) }
    var showJugadorSheet by remember { mutableStateOf(false) }
    var showGanadorSheet by remember { mutableStateOf(false) }

    LaunchedEffect(partidaId) {
        viewModel.onEvent(EditPartidaUiEvent.Load(partidaId ?: 0))
        if (state.isNew) viewModel.onEvent(EditPartidaUiEvent.FechaChanged(getFechaActual()))
    }


    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            navController.popBackStack()
        }
    }

    val jugador1Name =
        jugadoresState.jugadores.find { it.jugadorId == state.jugador1Id }?.nombres ?: ""
    val jugador2Name =
        jugadoresState.jugadores.find { it.jugadorId == state.jugador2Id }?.nombres ?: ""
    val ganadorName =
        jugadoresState.jugadores.find { it.jugadorId == state.ganadorId }?.nombres ?: ""

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (state.isNew) "Nueva Partida" else "Editar Partida") }) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = state.fecha,
                onValueChange = {},
                label = { Text("Fecha") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = jugador1Name,
                onValueChange = {},
                label = { Text("Jugador 1") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectingJugador1 = true
                        showJugadorSheet = true
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = jugador2Name,
                onValueChange = {},
                label = { Text("Jugador 2") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectingJugador1 = false
                        showJugadorSheet = true
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))


            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(
                    checked = state.esFinalizada,
                    onCheckedChange = { checked ->
                        viewModel.onEvent(EditPartidaUiEvent.EsFinalizadaChanged(checked))
                        if (!checked) {
                            viewModel.onEvent(EditPartidaUiEvent.GanadorChanged(null))
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Finalizada")
            }

            Spacer(modifier = Modifier.height(16.dp))


            if (state.esFinalizada) {
                OutlinedTextField(
                    value = ganadorName,
                    onValueChange = {},
                    label = { Text("Ganador") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showGanadorSheet = true }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.onEvent(EditPartidaUiEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(if (state.isNew) "Crear" else "Guardar", color = MaterialTheme.colorScheme.onPrimary)
                }

                if (!state.isNew) {
                    OutlinedButton(
                        onClick = { viewModel.onEvent(EditPartidaUiEvent.Delete) },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Eliminar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.saved) Text("Partida guardada correctamente!", color = MaterialTheme.colorScheme.primary)
            if (state.deleted) Text("Partida eliminada", color = MaterialTheme.colorScheme.error)
        }
    }

    if (showJugadorSheet) {
        JugadorSelectionSheet(
            jugadores = jugadoresState.jugadores,
            onDismiss = { showJugadorSheet = false },
            onJugadorSelected = { jugador ->
                if (selectingJugador1) {
                    viewModel.onEvent(EditPartidaUiEvent.Jugador1Changed(jugador.jugadorId))
                } else {
                    viewModel.onEvent(EditPartidaUiEvent.Jugador2Changed(jugador.jugadorId))
                }
                showJugadorSheet = false
            }
        )
    }

    if (showGanadorSheet) {
        val posiblesGanadores = listOfNotNull(
            jugadoresState.jugadores.find { it.jugadorId == state.jugador1Id },
            jugadoresState.jugadores.find { it.jugadorId == state.jugador2Id }
        )
        JugadorSelectionSheet(
            jugadores = posiblesGanadores,
            onDismiss = { showGanadorSheet = false },
            onJugadorSelected = { jugador ->
                viewModel.onEvent(EditPartidaUiEvent.GanadorChanged(jugador.jugadorId))
                showGanadorSheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorSelectionSheet(
    jugadores: List<Jugador>,
    onDismiss: () -> Unit,
    onJugadorSelected: (Jugador) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Selecciona un jugador", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(jugadores) { jugador ->
                    Text(
                        jugador.nombres,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onJugadorSelected(jugador)
                                onDismiss()
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
