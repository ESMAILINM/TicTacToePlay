package edu.ucne.TicTacToePlay.presentation.jugador.edit

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.material3.MaterialTheme
import org.junit.Rule
import org.junit.Test

class EditJugadorScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun userCanTypeAndSave() {
        var lastEvent: EditJugadorEvent? = null
        composeRule.setContent {
            MaterialTheme {
                EditJugadorBody(
                    state = EditJugadorUiState(isSaving = false, isDeleting = false),
                    onEvent = { lastEvent = it }
                )
            }
        }

        composeRule.onNodeWithTag("input_nombre").assertIsDisplayed().performTextInput("Juan")
        composeRule.onNodeWithTag("input_partidas").assertIsDisplayed().performTextInput("5")
        composeRule.onNodeWithTag("btn_guardar").performClick()

        assert(lastEvent is EditJugadorEvent.Save)
    }

    @Test
    fun showDeleteWhenCanBeDeleted_andClickEmitsDelete() {
        var lastEvent: EditJugadorEvent? = null
        composeRule.setContent {
            MaterialTheme {
                EditJugadorBody(
                    state = EditJugadorUiState(
                        jugadorId = 1,
                        nombre = "Luis",
                        partidas = "10",
                        canBeDeleted = true
                    ),
                    onEvent = { lastEvent = it }
                )
            }
        }

        composeRule.onNodeWithTag("btn_eliminar").assertIsDisplayed().performClick()
        assert(lastEvent is EditJugadorEvent.Delete)
    }

    @Test
    fun muestraErroresDeValidacion() {
        val state = EditJugadorUiState(
            nombre = "",
            partidas = "",
            nombreError = "El nombre es muy corto.",
            partidasError = "Las partidas deben ser un valor positivo.",
            canBeDeleted = false
        )

        composeRule.setContent {
            MaterialTheme {
                EditJugadorBody(
                    state = state,
                    onEvent = {}
                )
            }
        }

        composeRule.onNodeWithText("El nombre es muy corto.").assertIsDisplayed()
        composeRule.onNodeWithText("Las partidas deben ser un valor positivo.").assertIsDisplayed()
    }
}
