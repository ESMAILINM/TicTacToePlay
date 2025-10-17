package edu.ucne.TicTacToePlay.domain.usecase.logroUseCase

import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.model.Logro
import edu.ucne.TicTacToePlay.domain.repository.LogroRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetLogroUseCaseTest {

    private lateinit var repository: LogroRepository
    private lateinit var getLogroUseCase: GetLogroUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        getLogroUseCase = GetLogroUseCase(repository)
    }

    @Test
    fun `invoke returns logro when exists`() = runTest {
        val logro = Logro(logroId = 1, nombre = "Primer logro", descripcion = "Descripción")
        coEvery { repository.getLogro(1) } returns logro

        val result = getLogroUseCase(1)

        assertThat(result).isEqualTo(logro)
        coVerify { repository.getLogro(1) }
    }

    @Test
    fun `invoke returns null when logro does not exist`() = runTest {
        coEvery { repository.getLogro(42) } returns null

        val result = getLogroUseCase(42)

        assertThat(result).isNull()
        coVerify { repository.getLogro(42) }
    }
}
