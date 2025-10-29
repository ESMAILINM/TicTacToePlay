package edu.ucne.TicTacToePlay.domain.usecase.jugadorUseCase

import edu.ucne.TicTacToePlay.domain.model.Jugador
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.domain.validation.JugadorValidator
import edu.ucne.TicTacToePlay.tareas.remote.Resource
import javax.inject.Inject
class UpsertJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository,
    private val validator: JugadorValidator
) {
    suspend operator fun invoke(jugador: Jugador): Resource<Unit> {
//        val nombreResult = validator.validateNombreUnico(jugador.nombres, jugador.jugadorId)
//        if (!nombreResult.isValid) {
//            return Resource.Error(nombreResult.error ?: "Nombre inválido")
//        }

        return try {
            val jugadorPendiente = jugador.copy(isPendingCreate = true)
            repository.upsertJugador(jugadorPendiente)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error al guardar jugador")
        }
    }
}
