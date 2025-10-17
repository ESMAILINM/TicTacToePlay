package edu.ucne.TicTacToePlay.data.repository_tests

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.model.Logro
import edu.ucne.TicTacToePlay.tareas.local.dao.LogroDao
import edu.ucne.TicTacToePlay.tareas.local.entities.LogroEntity
import edu.ucne.TicTacToePlay.tareas.repository.LogroRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogroRepositoryImplTest {

    private lateinit var dao: LogroDao
    private lateinit var repository: LogroRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = LogroRepositoryImpl(dao)
    }

    @Test
    fun observeLogros_mapsEntitiesToDomain() = runTest {
        val shared = MutableSharedFlow<List<LogroEntity>>()
        every { dao.observeAll() } returns shared

        val job = launch {
            repository.observeLogros().test {
                // Primera emisión
                shared.emit(listOf(LogroEntity(logroId = 1, nombre = "Principiante", descripcion = "Primer logro")))
                val first = awaitItem()
                assertThat(first).containsExactly(Logro(logroId = 1, nombre = "Principiante", descripcion = "Primer logro"))

                // Segunda emisión
                shared.emit(
                    listOf(
                        LogroEntity(logroId = 2, nombre = "Intermedio", descripcion = "Segundo logro"),
                        LogroEntity(logroId = 3, nombre = "Avanzado", descripcion = "Tercer logro")
                    )
                )
                val second = awaitItem()
                assertThat(second).containsExactly(
                    Logro(logroId = 2, nombre = "Intermedio", descripcion = "Segundo logro"),
                    Logro(logroId = 3, nombre = "Avanzado", descripcion = "Tercer logro")
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
        job.join()
    }

    @Test
    fun getLogro_returnsMappedDomainModel_whenEntityExists() = runTest {
        coEvery { dao.getById(5) } returns LogroEntity(logroId = 5, nombre = "Experto", descripcion = "Logro avanzado")

        val result = repository.getLogro(5)

        assertThat(result).isEqualTo(Logro(logroId = 5, nombre = "Experto", descripcion = "Logro avanzado"))
    }

    @Test
    fun getLogro_returnsNull_whenEntityMissing() = runTest {
        coEvery { dao.getById(42) } returns null

        val result = repository.getLogro(42)

        assertThat(result).isNull()
    }

    @Test
    fun upsertLogro_callsDaoWithMappedEntity_andReturnsLogroId() = runTest {
        coEvery { dao.upsert(any()) } returns 10L
        val logro = Logro(logroId = 10, nombre = "Maestro", descripcion = "Nuevo logro")

        val returnedId = repository.upsertLogro(logro)

        assertThat(returnedId).isEqualTo(10)
        coVerify { dao.upsert(LogroEntity(logroId = 10, nombre = "Maestro", descripcion = "Nuevo logro")) }
    }

    @Test
    fun deleteLogro_callsDaoDeleteById() = runTest {
        coEvery { dao.deleteById(12) } just runs

        repository.deleteLogro(12)

        coVerify { dao.deleteById(12) }
    }
}
