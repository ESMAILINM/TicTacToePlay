package edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ObserveJugadorUseCaseTest {

    private lateinit var repository: JugadorRepository
    private lateinit var observeJugadorUseCase: ObserveJugadorUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        observeJugadorUseCase = ObserveJugadorUseCase(repository)
    }

    @Test
    fun `invoke emits jugadores from repository`() = runTest {
        val shared = MutableSharedFlow<List<Jugador>>()
        every { repository.observeJugador() } returns shared

        val job = launch {
            observeJugadorUseCase().test {

                val jugador1 = Jugador(jugadorId = 1, nombres = "Juan", partidas = 3)
                shared.emit(listOf(jugador1))
                val first = awaitItem()
                assertThat(first).containsExactly(jugador1)

                val jugador2 = Jugador(jugadorId = 2, nombres = "Ana", partidas = 5)
                shared.emit(listOf(jugador1, jugador2))
                val second = awaitItem()
                assertThat(second).containsExactly(jugador1, jugador2)

                cancelAndIgnoreRemainingEvents()
            }
        }
        job.join()
    }
}
