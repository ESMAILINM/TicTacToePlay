package edu.ucne.TicTacToePlay.domain.usecase.partidaUseCase

import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.model.Partida
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import edu.ucne.TicTacToePlay.domain.validation.PartidaValidator
import edu.ucne.TicTacToePlay.domain.validation.ValidationResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpsertPartidaUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var validator: PartidaValidator
    private lateinit var upsertPartidaUseCase: UpsertPartidaUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        validator = mockk()
        upsertPartidaUseCase = UpsertPartidaUseCase(repository, validator)
    }

    @Test
    fun `invoke succeeds when validations pass`() = runTest {
        val partida = Partida(
            partidaId = 1,
            fecha = "2025-09-27",
            jugador1Id = 1,
            jugador2Id = 2,
            ganadorId = 1,
            esFinalizada = true
        )

        coEvery { validator.validateJugadores(1,2).isValid } returns true
        coEvery { validator.validateGanador(1,1,2).isValid } returns true
        coEvery { validator.validateFecha("2025-09-27").isValid } returns true
        coEvery { repository.upsert(partida) } returns 1

        val result = upsertPartidaUseCase(partida)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(1)
        coVerify { repository.upsert(partida) }
    }

    @Test
    fun `invoke fails when jugador validation fails`() = runTest {
        val partida = Partida(1,"2025-09-27",1,2,1,true)
        coEvery { validator.validateJugadores(1,2) } returns ValidationResult(false,"Jugadores inválidos")

        val result = upsertPartidaUseCase(partida)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Jugadores inválidos")
    }

    @Test
    fun `invoke fails when ganador validation fails`() = runTest {
        val partida = Partida(1,"2025-09-27",1,2,3,true)
        coEvery { validator.validateJugadores(1,2) } returns ValidationResult(true)
        coEvery { validator.validateGanador(3,1,2) } returns ValidationResult(false,"Ganador inválido")

        val result = upsertPartidaUseCase(partida)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Ganador inválido")
    }

    @Test
    fun `invoke fails when fecha validation fails`() = runTest {
        val partida = Partida(1,"2025-09-40",1,2,1,true)
        coEvery { validator.validateJugadores(1,2) } returns ValidationResult(true)
        coEvery { validator.validateGanador(1,1,2) } returns ValidationResult(true)
        coEvery { validator.validateFecha("2025-09-40") } returns ValidationResult(
            false,
            "Fecha inválida"
        )

        val result = upsertPartidaUseCase(partida)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Fecha inválida")
    }
}
