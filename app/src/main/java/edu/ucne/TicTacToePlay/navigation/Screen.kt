// Screen.kt
package edu.ucne.TicTacToePlay.navigation

sealed class Screen(val route: String) {
    object ListJugador : Screen("list_jugador_screen")
    object ListPartida : Screen("list_partida_screen")
    object ListLogro : Screen("list_logro_screen")

    object Jugador : Screen("edit_jugador_screen/{jugadorId}") {
        fun createRoute(jugadorId: String) = "edit_jugador_screen/$jugadorId"
    }

    object Partida : Screen("edit_partida_screen/{partidaId}") {
        fun createRoute(partidaId: Int) = "edit_partida_screen/$partidaId"
    }

    object Logro : Screen("edit_logro_screen/{logroId}") {
        fun createRoute(logroId: Int) = "edit_logro_screen/$logroId"
    }
    object TicTacToe : Screen("tictactoe_screen/{jugadorId}")
}
