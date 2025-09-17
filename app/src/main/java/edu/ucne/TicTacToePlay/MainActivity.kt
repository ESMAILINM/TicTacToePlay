package edu.ucne.TicTacToePlay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.TicTacToePlay.presentation.jugador.edit.EditJugadorScreen
import edu.ucne.TicTacToePlay.presentation.jugador.list.ListJugadorScreen
import edu.ucne.TicTacToePlay.presentation.partida.EditPartidaScreen
import edu.ucne.TicTacToePlay.presentation.partida.list.ListPartidaScreen
import edu.ucne.TicTacToePlay.ui.theme.TicTacToePlayTheme
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToePlayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "list_jugador_screen"
                    ) {
                        composable("list_jugador_screen") {
                            ListJugadorScreen(navController = navController)
                        }
                        composable(
                            route = "edit_jugador_screen/{jugadorId}",
                            arguments = listOf(
                                navArgument("jugadorId") {
                                    type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val jugadorId = backStackEntry.arguments?.getInt("jugadorId")
                            EditJugadorScreen(
                                navController = navController,
                                jugadorId = jugadorId
                            )
                        }
                        composable("list_partida_screen") {
                            ListPartidaScreen(navController = navController)
                        }
                        composable(
                            route = "edit_partida_screen/{partidaId}",
                            arguments = listOf(navArgument("partidaId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val partidaId = backStackEntry.arguments?.getInt("partidaId")
                            EditPartidaScreen(navController = navController, partidaId = partidaId)
                        }


                    }

                }
            }
        }
    }
}