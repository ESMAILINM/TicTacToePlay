package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPartidaUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var getPartidaUseCase: GetPartidaUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        getPartidaUseCase = GetPartidaUseCase(repository)
    }

    @Test
    fun `invoke returns partida when exists`() = runTest {
        val partida = Partida(
            partidaId = 1,
            fecha = "2025-09-27",
            jugador1Id = 1,
            jugador2Id = 2,
            ganadorId = 1,
            esFinalizada = true
        )
        coEvery { repository.getPartida(1) } returns partida

        val result = getPartidaUseCase(1)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(partida)
        coVerify { repository.getPartida(1) }
    }

    @Test
    fun `invoke returns null when partida does not exist`() = runTest {
        coEvery { repository.getPartida(42) } returns null

        val result = getPartidaUseCase(42)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNull()
        coVerify { repository.getPartida(42) }
    }

    @Test
    fun `invoke fails when repository throws exception`() = runTest {
        coEvery { repository.getPartida(3) } throws RuntimeException("Error al obtener")

        val result = getPartidaUseCase(3)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Error al obtener")
        coVerify { repository.getPartida(3) }
    }
}
