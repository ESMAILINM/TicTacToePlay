package edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase

import com.google.common.truth.Truth.assertThat
import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.domain.validation.JugadorValidator
import edu.ucne.TicTacToePlay.domain.validation.ValidationResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpsertJugadorUseCaseTest {

    private lateinit var repository: JugadorRepository
    private lateinit var validator: JugadorValidator
    private lateinit var upsertJugadorUseCase: UpsertJugadorUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        validator = mockk()
        upsertJugadorUseCase = UpsertJugadorUseCase(repository, validator)
    }

    @Test
    fun `invoke succeeds when validations pass`() = runTest {
        val jugador = Jugador(jugadorId = 1, nombres = "Juan", partidas = 3)

        coEvery { validator.validateNombreUnico("Juan", 1) } returns ValidationResult(true)
        coEvery { validator.validatePartida("3") } returns ValidationResult(true)
        coEvery { repository.upsertJugador(jugador) } returns 1

        val result = upsertJugadorUseCase(jugador)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(1)
        coVerify { repository.upsertJugador(jugador) }
    }

    @Test
    fun `invoke fails when nombre is not unique`() = runTest {
        val jugador = Jugador(jugadorId = 2, nombres = "Ana", partidas = 3)

        coEvery { validator.validateNombreUnico("Ana", 2) } returns ValidationResult(false, "Ya existe un jugador con este nombre")

        val result = upsertJugadorUseCase(jugador)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Ya existe un jugador con este nombre")
    }

    @Test
    fun `invoke fails when partidas are invalid`() = runTest {
        val jugador = Jugador(jugadorId = 3, nombres = "Luis", partidas = -1)

        coEvery { validator.validateNombreUnico("Luis", 3) } returns ValidationResult(true)
        coEvery { validator.validatePartida("-1") } returns ValidationResult(false, "Debe ser un valor positivo.")

        val result = upsertJugadorUseCase(jugador)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Debe ser un valor positivo.")
    }
}
