package edu.ucne.TicTacToePlay.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import edu.ucne.TicTacToePlay.presentation.jugador.edit.EditJugadorScreen
import edu.ucne.TicTacToePlay.presentation.jugador.list.ListJugadorScreen
import edu.ucne.TicTacToePlay.presentation.partida.EditPartidaScreen
import edu.ucne.TicTacToePlay.presentation.partida.list.ListPartidaScreen
import edu.ucne.TicTacToePlay.presentation.logro.edit.EditLogroScreen
import edu.ucne.TicTacToePlay.presentation.logro.list.ListLogroScreen
import edu.ucne.TicTacToePlay.presentation.tictactoe.TicTacToeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeNavHost(navHostController: NavHostController) {
 val scope = rememberCoroutineScope()
 val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
 val selectedItem = remember { mutableStateOf(Screen.ListJugador.route) }

 fun handleItemClick(screen: Screen) {
  navHostController.navigate(screen.route) {
   popUpTo(navHostController.graph.findStartDestination().id) { saveState = true }
   launchSingleTop = true
   restoreState = true
  }
  selectedItem.value = screen.route
  scope.launch { drawerState.close() }
 }

 val currentScreenIconAndTitle = when (selectedItem.value) {
  Screen.ListJugador.route -> Icons.Filled.Person to "Jugadores"
  Screen.Jugador.route -> Icons.Filled.Person to "Editar Jugador"
  Screen.ListPartida.route -> Icons.Filled.SportsEsports to "Partidas"
  Screen.Partida.route -> Icons.Filled.SportsEsports to "Editar Partida"
  Screen.ListLogro.route -> Icons.Filled.Star to "Logros"
  Screen.Logro.route -> Icons.Filled.Star to "Editar Logro"
  else -> Icons.Filled.VideogameAsset to "Tic Tac Toe"
 }

 ModalNavigationDrawer(
  drawerContent = {
   ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
     text = "Tic Tac Toe Play",
     style = MaterialTheme.typography.headlineMedium,
     fontWeight = FontWeight.Bold,
     color = Color.DarkGray,
     modifier = Modifier.padding(16.dp)
    )
    HorizontalDivider()
    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn {
     item {
      DrawerItem(
       title = "Jugadores",
       icon = Icons.Filled.Person,
       isSelected = selectedItem.value == Screen.ListJugador.route,
       screen = Screen.ListJugador
      ) { handleItemClick(it) }

      DrawerItem(
       title = "Partidas",
       icon = Icons.Filled.SportsEsports,
       isSelected = selectedItem.value == Screen.ListPartida.route,
       screen = Screen.ListPartida
      ) { handleItemClick(it) }

      DrawerItem(
       title = "Logros",
       icon = Icons.Filled.Star,
       isSelected = selectedItem.value == Screen.ListLogro.route,
       screen = Screen.ListLogro
      ) { handleItemClick(it) }
     }
    }
   }
  },
  drawerState = drawerState
 ) {
  Scaffold(
   topBar = {
    TopAppBar(
     title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
       Text(currentScreenIconAndTitle.second)
//       Icon(
//        imageVector = currentScreenIconAndTitle.first,
//        contentDescription = null,
//        modifier = Modifier.padding(end = 8.dp)
//       )
      }
     },
     navigationIcon = {
      IconButton(onClick = { scope.launch { drawerState.open() } }) {
       Icon(Icons.Filled.Menu, contentDescription = "Menu")
      }
     }
    )
   }
  ) { innerPadding ->
   NavHost(
    navController = navHostController,
    startDestination = Screen.ListJugador.route,
    modifier = Modifier.padding(innerPadding)
   ) {

    composable(Screen.ListJugador.route) {
     selectedItem.value = Screen.ListJugador.route
     ListJugadorScreen(navController = navHostController)
    }

    composable(
     route = Screen.Jugador.route,
     arguments = listOf(navArgument("jugadorId") { type = androidx.navigation.NavType.IntType })
    ) { backStackEntry ->
     val id = backStackEntry.arguments?.getInt("jugadorId")
     selectedItem.value = Screen.Jugador.route
     EditJugadorScreen(navController = navHostController, jugadorId = id.toString())
    }

    composable(Screen.ListPartida.route) {
     selectedItem.value = Screen.ListPartida.route
     ListPartidaScreen(navController = navHostController)
    }

//    composable(
//     route = Screen.Partida.route,
//     arguments = listOf(navArgument("partidaId") { type = androidx.navigation.NavType.IntType })
//    ) { backStackEntry ->
//     val id = backStackEntry.arguments?.getInt("partidaId")
//     selectedItem.value = Screen.Partida.route
//     EditPartidaScreen(navController = navHostController, partidaId = id)
//    }

    composable(Screen.ListLogro.route) {
     selectedItem.value = Screen.ListLogro.route
     ListLogroScreen(navController = navHostController)
    }

    composable(
     route = Screen.Logro.route,
     arguments = listOf(navArgument("logroId") { type = androidx.navigation.NavType.IntType })
    ) { backStackEntry ->
     val id = backStackEntry.arguments?.getInt("logroId")
     selectedItem.value = Screen.Logro.route
     EditLogroScreen(navController = navHostController, logroId = id)
    }

    composable(
     route = Screen.Jugador.route,
     arguments = listOf(navArgument("jugadorId") { type = NavType.StringType })
    ) { backStackEntry ->
     val id = backStackEntry.arguments?.getString("jugadorId")
     EditJugadorScreen(navController = navHostController, jugadorId = id)
    }
   }


   }
  }
 }
@Preview(showBackground = true, widthDp = 400, heightDp = 700)
@Composable
fun TicTacToeNavHostPreview() {
 val navController = rememberNavController()
 TicTacToeNavHost(navHostController = navController)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeTopAppBar(
 icon: ImageVector,
 title: String,
 onMenuClick: () -> Unit = {}
) {
 TopAppBar(
  title = {
   Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(
     imageVector = icon,
     contentDescription = null,
     modifier = Modifier.padding(end = 8.dp)
    )
    Text(title)
   }
  },
  navigationIcon = {
   IconButton(onClick = onMenuClick) {
    Icon(Icons.Filled.ArrowBack, contentDescription = "Menu")
   }
  }
 )
}

@Preview(showBackground = true)
@Composable
fun TicTacToeTopAppBarPreview() {
 TicTacToeTopAppBar(
  icon = Icons.Filled.Person,
  title = "Jugadores"
 )
}
