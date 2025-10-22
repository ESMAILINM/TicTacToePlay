package edu.ucne.TicTacToePlay.domain.usecase.logroUseCase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.model.Logro
import edu.ucne.TicTacToePlay.domain.repository.LogroRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ObserveLogroUseCaseTest {

    private lateinit var repository: LogroRepository
    private lateinit var observeLogroUseCase: ObserveLogroUseCase

    @Before
    fun setUp() {
        repository = mockk()
        observeLogroUseCase = ObserveLogroUseCase(repository)
    }

    @Test
    fun `invoke emits list of logros`() = runTest {
        val shared = MutableSharedFlow<List<Logro>>()
        every { repository.observeLogros() } returns shared

        val job = launch {
            observeLogroUseCase().test {
                val logro1 = Logro(logroId = 1, nombre = "Primer logro", descripcion = "Desc 1")
                shared.emit(listOf(logro1))
                val first = awaitItem()
                assertThat(first).containsExactly(logro1)

                val logro2 = Logro(logroId = 2, nombre = "Segundo logro", descripcion = "Desc 2")
                shared.emit(listOf(logro1, logro2))
                val second = awaitItem()
                assertThat(second).containsExactly(logro1, logro2)

                cancelAndIgnoreRemainingEvents()
            }
        }

        job.join()
    }
}
