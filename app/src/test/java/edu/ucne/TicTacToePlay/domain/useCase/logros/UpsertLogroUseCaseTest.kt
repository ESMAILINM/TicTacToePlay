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

class UpsertLogroUseCaseTest {

    private lateinit var repository: LogroRepository
    private lateinit var upsertLogroUseCase: UpsertLogroUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        upsertLogroUseCase = UpsertLogroUseCase(repository)
    }

    @Test
    fun `invoke succeeds when nombre and descripcion are valid`() = runTest {
        val logro = Logro(logroId = 1, nombre = "Primer logro", descripcion = "Descripción")
        coEvery { repository.upsertLogro(logro) } returns 1

        val result = upsertLogroUseCase(logro)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(1)
        coVerify { repository.upsertLogro(logro) }
    }

    @Test
    fun `invoke fails when descripcion is empty`() = runTest {
        val logro = Logro(logroId = 2, nombre = "Segundo", descripcion = "")

        val result = upsertLogroUseCase(logro)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("La descripción no puede estar vacía")
    }

    @Test
    fun `invoke fails when nombre is empty`() = runTest {
        val logro = Logro(logroId = 3, nombre = "", descripcion = "Descripción")

        val result = upsertLogroUseCase(logro)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("El nombre no puede estar vacío")
    }
}
