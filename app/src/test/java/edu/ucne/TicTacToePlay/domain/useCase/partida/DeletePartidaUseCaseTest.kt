package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeletePartidaUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var deletePartidaUseCase: DeletePartidaUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        deletePartidaUseCase = DeletePartidaUseCase(repository)
    }

    @Test
    fun `invoke succeeds when repository deletes partida`() = runTest {
        val partidaId = 1
        coEvery { repository.delete(partidaId) } returns Unit

        val result = deletePartidaUseCase(partidaId)

        assertThat(result.isSuccess).isTrue()
        coVerify(exactly = 1) { repository.delete(partidaId) }
    }

    @Test
    fun `invoke fails when repository throws exception`() = runTest {
        val partidaId = 2
        coEvery { repository.delete(partidaId) } throws RuntimeException("Error al eliminar")

        val result = deletePartidaUseCase(partidaId)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Error al eliminar")
        coVerify(exactly = 1) { repository.delete(partidaId) }
    }
}
