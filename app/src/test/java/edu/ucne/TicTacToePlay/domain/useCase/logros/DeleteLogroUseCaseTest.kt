package edu.ucne.TicTacToePlay.domain.usecase.logroUseCase

import edu.ucne.TicTacToePlay.domain.repository.LogroRepository
import io.mockk.coVerify
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteLogroUseCaseTest {

    private lateinit var repository: LogroRepository
    private lateinit var deleteLogroUseCase: DeleteLogroUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        deleteLogroUseCase = DeleteLogroUseCase(repository)
    }

    @Test
    fun `invoke calls repository deleteLogro`() = runTest {
        val logroId = 5
        coEvery { repository.deleteLogro(logroId) } returns Unit

        deleteLogroUseCase(logroId)

        coVerify(exactly = 1) { repository.deleteLogro(logroId) }
    }
}
