package edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase

import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class GetJugadorUseCaseTest {

    private lateinit var repository: JugadorRepository
    private lateinit var getJugadorUseCase: GetJugadorUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        getJugadorUseCase = GetJugadorUseCase(repository)
    }

    @Test
    fun `invoke returns jugador when repository has it`() = runTest {
        val jugador = Jugador(jugadorId = 1, nombres = "Juan", partidas = 5)
        coEvery { repository.getJugador(1) } returns jugador

        val result = getJugadorUseCase(1)

        assertThat(result).isEqualTo(jugador)
        coVerify { repository.getJugador(1) }
    }

    @Test
    fun `invoke returns null when repository does not have jugador`() = runTest {
        coEvery { repository.getJugador(42) } returns null

        val result = getJugadorUseCase(42)

        assertThat(result).isNull()
        coVerify { repository.getJugador(42) }
    }
}
