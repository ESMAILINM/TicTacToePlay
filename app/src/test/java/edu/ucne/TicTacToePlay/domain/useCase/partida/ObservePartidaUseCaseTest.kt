package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ObservePartidaUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var observePartidaUseCase: ObservePartidaUseCase

    @Before
    fun setUp() {
        repository = mockk()
        observePartidaUseCase = ObservePartidaUseCase(repository)
    }

    @Test
    fun `invoke emits list of partidas`() = runTest {
        val shared = MutableSharedFlow<List<Partida>>()
        every { repository.observePartidas() } returns shared

        val job = launch {
            observePartidaUseCase().test {
                val p1 = Partida(
                    partidaId = 1,
                    fecha = "2025-09-27",
                    jugador1Id = 1,
                    jugador2Id = 2,
                    ganadorId = 1,
                    esFinalizada = true
                )
                shared.emit(listOf(p1))
                val first = awaitItem()
                assertThat(first).containsExactly(p1)

                val p2 = Partida(
                    partidaId = 2,
                    fecha = "2025-09-28",
                    jugador1Id = 3,
                    jugador2Id = 4,
                    ganadorId = 4,
                    esFinalizada = true
                )
                shared.emit(listOf(p1, p2))
                val second = awaitItem()
                assertThat(second).containsExactly(p1, p2)

                cancelAndIgnoreRemainingEvents()
            }
        }

        job.join()
    }
}
