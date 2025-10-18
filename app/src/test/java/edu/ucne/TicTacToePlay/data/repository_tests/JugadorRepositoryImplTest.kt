package edu.ucne.TicTacToePlay.data.repository_tests

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.tareas.local.dao.JugadorDao
import edu.ucne.TicTacToePlay.tareas.local.entities.JugadorEntity
import edu.ucne.TicTacToePlay.tareas.repository.JugadorRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JugadorRepositoryImplTest {

    private lateinit var dao: JugadorDao
    private lateinit var repository: JugadorRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = JugadorRepositoryImpl(dao)
    }

    @Test
    fun observeJugador_mapsEntitiesToDomain() = runTest {
        val shared = MutableSharedFlow<List<JugadorEntity>>()
        every { dao.observeAll() } returns shared

        repository.observeJugador().test {
            shared.emit(listOf(JugadorEntity(jugadorId = 1, nombres = "Juan", partidas = 5)))
            val first = awaitItem()
            assertThat(first).containsExactly(Jugador(jugadorId = 1, nombres = "Juan", partidas = 5))

            shared.emit(
                listOf(
                    JugadorEntity(jugadorId = 2, nombres = "Ana", partidas = 3),
                    JugadorEntity(jugadorId = 3, nombres = "Luis", partidas = 4)
                )
            )
            val second = awaitItem()
            assertThat(second).containsExactly(
                Jugador(jugadorId = 2, nombres = "Ana", partidas = 3),
                Jugador(jugadorId = 3, nombres = "Luis", partidas = 4)
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getJugador_returnsMappedDomainModel_whenEntityExists() = runTest {
        coEvery { dao.getById(5) } returns JugadorEntity(jugadorId = 5, nombres = "Pedro", partidas = 7)

        val result = repository.getJugador(5)

        assertThat(result).isEqualTo(Jugador(jugadorId = 5, nombres = "Pedro", partidas = 7))
    }

    @Test
    fun getJugador_returnsNull_whenEntityMissing() = runTest {
        coEvery { dao.getById(42) } returns null

        val result = repository.getJugador(42)

        assertThat(result).isNull()
    }

    @Test
    fun upsertJugador_callsDaoWithMappedEntity_andReturnsJugadorId() = runTest {
        coEvery { dao.upsert(any()) } just runs
        val jugador = Jugador(jugadorId = 10, nombres = "Mario", partidas = 1)

        val returnedId = repository.upsertJugador(jugador)

        assertThat(returnedId).isEqualTo(10)
        coVerify { dao.upsert(JugadorEntity(jugadorId = 10, nombres = "Mario", partidas = 1)) }
    }

    @Test
    fun deleteJugador_callsDaoDeleteById() = runTest {
        coEvery { dao.delete(12) } just runs

        repository.deleteJugador(12)

        coVerify { dao.delete(12) }
    }

    @Test
    fun getJugadorByName_returnsMappedDomainModel_whenEntityExists() = runTest {
        coEvery { dao.getJugadorByName("Lucía") } returns Jugador(jugadorId = 7, nombres = "Lucía", partidas = 9)

        val result = repository.getJugadorByName("Lucía")

        assertThat(result).isEqualTo(Jugador(jugadorId = 7, nombres = "Lucía", partidas = 9))
    }

    @Test
    fun getJugadorByName_returnsNull_whenEntityMissing() = runTest {
        coEvery { dao.getJugadorByName("Desconocido") } returns null

        val result = repository.getJugadorByName("Desconocido")

        assertThat(result).isNull()
    }
}
