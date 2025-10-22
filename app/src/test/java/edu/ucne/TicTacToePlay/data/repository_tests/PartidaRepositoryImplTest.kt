package edu.ucne.TicTacToePlay.data.repository_tests

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.data.repository.PartidaRepositoryImpl
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.tareas.local.dao.PartidaDao
import edu.ucne.TicTacToePlay.tareas.local.entities.PartidaEntity
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
class PartidaRepositoryImplTest {

    private lateinit var dao: PartidaDao
    private lateinit var repository: PartidaRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = PartidaRepositoryImpl(dao)
    }

    @Test
    fun observePartidas_mapsEntitiesToDomain() = runTest {
        val shared = MutableSharedFlow<List<PartidaEntity>>()
        every { dao.observeAll() } returns shared

        val job = launch {
            repository.observePartidas().test {
                // Primera emisión
                shared.emit(
                    listOf(
                        PartidaEntity(
                            partidaId = 1,
                            fecha = "2025-09-27T16:00:00",
                            jugador1Id = 101,
                            jugador2Id = 102,
                            ganadorId = 101,
                            esFinalizada = true
                        )
                    )
                )
                val first = awaitItem()
                assertThat(first).containsExactly(
                    Partida(
                        partidaId = 1,
                        fecha = "2025-09-27T16:00:00",
                        jugador1Id = 101,
                        jugador2Id = 102,
                        ganadorId = 101,
                        esFinalizada = true
                    )
                )

                // Segunda emisión
                shared.emit(
                    listOf(
                        PartidaEntity(
                            partidaId = 2,
                            fecha = "2025-09-27T16:00:00",
                            jugador1Id = 103,
                            jugador2Id = 104,
                            ganadorId = 104,
                            esFinalizada = true
                        ),
                        PartidaEntity(
                            partidaId = 3,
                            fecha = "2025-09-27T16:00:00",
                            jugador1Id = 105,
                            jugador2Id = 106,
                            ganadorId = 105,
                            esFinalizada = false
                        )
                    )
                )
                val second = awaitItem()
                assertThat(second).containsExactly(
                    Partida(
                        partidaId = 2,
                        fecha = "2025-09-27T16:00:00",
                        jugador1Id = 103,
                        jugador2Id = 104,
                        ganadorId = 104,
                        esFinalizada = true
                    ),
                    Partida(
                        partidaId = 3,
                        fecha = "2025-09-27T16:00:00",
                        jugador1Id = 105,
                        jugador2Id = 106,
                        ganadorId = 105,
                        esFinalizada = false
                    )
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
        job.join()
    }

    @Test
    fun getPartida_returnsMappedDomainModel_whenEntityExists() = runTest {
        coEvery { dao.getById(5) } returns PartidaEntity(
            partidaId = 5,
            fecha = "2025-09-27T16:00:00",
            jugador1Id = 201,
            jugador2Id = 202,
            ganadorId = 202,
            esFinalizada = true
        )

        val result = repository.getPartida(5)

        assertThat(result).isEqualTo(
            Partida(
                partidaId = 5,
                fecha = "2025-09-27T16:00:00",
                jugador1Id = 201,
                jugador2Id = 202,
                ganadorId = 202,
                esFinalizada = true
            )
        )
    }

    @Test
    fun getPartida_returnsNull_whenEntityMissing() = runTest {
        coEvery { dao.getById(42) } returns null

        val result = repository.getPartida(42)

        assertThat(result).isNull()
    }

    @Test
    fun upsert_callsDaoWithMappedEntity_andReturnsPartidaId() = runTest {
        coEvery { dao.upsert(any()) } returns 10L
        val partida = Partida(
            partidaId = 10,
            fecha = "2025-09-27T16:00:00",
            jugador1Id = 301,
            jugador2Id = 302,
            ganadorId = 301,
            esFinalizada = false
        )

        val returnedId = repository.upsert(partida)

        assertThat(returnedId).isEqualTo(10)
        coVerify {
            dao.upsert(
                PartidaEntity(
                    partidaId = 10,
                    fecha = "2025-09-27T16:00:00",
                    jugador1Id = 301,
                    jugador2Id = 302,
                    ganadorId = 301,
                    esFinalizada = false
                )
            )
        }
    }

    @Test
    fun deletePartida_callsDaoDeleteById() = runTest {
        coEvery { dao.deleteById(12) } just runs

        repository.delete(12)

        coVerify { dao.deleteById(12) }
    }
}
