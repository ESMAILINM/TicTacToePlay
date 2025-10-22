package edu.ucne.TicTacToePlay.presentation.tictactoe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.TicTacToePlay.domain.model.Player
import edu.ucne.TicTacToePlay.domain.usecase.usecaseapi.GetPartidaApiUseCase
import edu.ucne.TicTacToePlay.domain.usecase.usecaseapi.PostMovimientoUseCase
import edu.ucne.TicTacToePlay.domain.usecase.usecaseapi.PostPartidaUseCase
import edu.ucne.TicTacToePlay.domain.usecase.usecaseapi.SaveMovimientosUseCase
import edu.ucne.TicTacToePlay.tareas.remote.dto.MovimientoDto
import edu.ucne.TicTacToePlay.tareas.remote.dto.PartidaDto
import edu.ucne.TicTacToePlay.tareas.remote.toDomainList
import edu.ucne.TicTacToePlay.tareas.remote.toDtoList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val postPartida: PostPartidaUseCase,
    private val postMovimiento: PostMovimientoUseCase,
    private val getPartida: GetPartidaApiUseCase,
    private val saveMovimientos: SaveMovimientosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state = _state.asStateFlow()

    private var jugador1Id = 1
    private var jugador2Id = 2
    private var partidaId: Int? = null

    fun selectPlayer(player: Player) {
        _state.update { it.copy(playerSelection = player, currentPlayer = player) }
    }

    fun startGame() {
        _state.update { it.copy(gameStarted = true) }
    }

    fun onCellClick(index: Int) {
        val stateValue = _state.value
        if (stateValue.board[index] != null || stateValue.winner != null) return

        val newBoard = stateValue.board.toMutableList().apply { this[index] = stateValue.currentPlayer }
        _state.update { it.copy(board = newBoard) }

        val fila = index / 3 + 1
        val columna = index % 3 + 1

        viewModelScope.launch {
            val pid = partidaId ?: run {
                val result = postPartida(jugador1Id, jugador2Id)
                val newId = when (result) {
                    is Int -> result
                    is Long -> result.toInt()
                    is PartidaDto -> result.partidaId
                    else -> null
                }
                newId?.also { partidaId = it } ?: return@launch
            }

            val movimiento = MovimientoDto(
                jugador = stateValue.currentPlayer.symbol,
                posicionFila = fila,
                posicionColumna = columna,
                partidaId = pid
            )

            postMovimiento(movimiento)

            val nextPlayer = if (stateValue.currentPlayer == Player.X) Player.O else Player.X
            _state.update { it.copy(currentPlayer = nextPlayer) }

            if (checkWinner()) _state.update { it.copy(winner = stateValue.currentPlayer) }
            else if (isBoardFull()) _state.update { it.copy(isDraw = true) }
        }
    }

    fun loadPartida(id: String) {
        val pid = id.toIntOrNull() ?: return
        partidaId = pid

        viewModelScope.launch {
            val movimientos = getPartida(pid).toDomainList()
            val newBoard = MutableList<Player?>(9) { null }

            movimientos.forEach { mov ->
                val fila = mov.posicionFila - 1
                val columna = mov.posicionColumna - 1
                if (fila in 0..2 && columna in 0..2) {
                    newBoard[fila * 3 + columna] = if (mov.jugador == "X") Player.X else Player.O
                }
            }

            val movesCount = newBoard.count { it != null }
            val starting = _state.value.playerSelection ?: Player.X
            val current = if (movesCount % 2 == 0) starting else if (starting == Player.X) Player.O else Player.X

            val winnerPlayer = findWinner(newBoard)
            val isDraw = newBoard.all { it != null } && winnerPlayer == null

            _state.update {
                it.copy(
                    board = newBoard,
                    currentPlayer = current,
                    gameStarted = true,
                    winner = winnerPlayer,
                    isDraw = isDraw
                )
            }
        }
    }

    fun saveMovimientosExistentes() {
        val pid = partidaId ?: return
        viewModelScope.launch {
            val movimientosDto = _state.value.toDtoList(pid)
            if (movimientosDto.isNotEmpty()) saveMovimientos(pid, movimientosDto)
        }
    }

    fun restartGame() {
        _state.value = GameUiState(
            playerSelection = _state.value.playerSelection,
            currentPlayer = _state.value.playerSelection ?: Player.X,
            gameStarted = true
        )
    }

    private fun checkWinner(): Boolean {
        val board = _state.value.board
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        return lines.any { (a, b, c) ->
            board[a] != null && board[a] == board[b] && board[a] == board[c]
        }
    }

    private fun findWinner(board: List<Player?>): Player? {
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (line in lines) {
            val (a, b, c) = line
            val p = board[a]
            if (p != null && p == board[b] && p == board[c]) return p
        }
        return null
    }

    private fun isBoardFull() = _state.value.board.all { it != null }
}
