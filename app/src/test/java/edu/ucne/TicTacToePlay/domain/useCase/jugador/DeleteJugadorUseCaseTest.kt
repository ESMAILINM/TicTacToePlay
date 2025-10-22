package edu.ucne.TicTacToePlay.domain.useCase.jugadorUseCase

import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase.DeleteJugadorUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteJugadorUseCaseTest {

    private lateinit var repository: JugadorRepository
    private lateinit var deleteJugadorUseCase: DeleteJugadorUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        deleteJugadorUseCase = DeleteJugadorUseCase(repository)
    }

    @Test
    fun `invoke calls repository deleteJugador with correct id`() = runTest {
        val jugadorId = 5

        deleteJugadorUseCase(jugadorId)

        coVerify { repository.deleteJugador(jugadorId) }
    }
}
