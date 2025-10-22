package edu.ucne.TicTacToePlay.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    screen: Screen,
    navigateTo: (Screen) -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(imageVector = icon,
                contentDescription = title,
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant) },

        label = { Text(text = title) },
        selected = isSelected,
        onClick = { navigateTo(screen) }
    )
}
@Preview(showBackground = true)
@Composable
fun DrawerItemPreview() {
    DrawerItem(
        title = "Jugadores",
        icon = Icons.Filled.Person,
        isSelected = true,
        screen = Screen.ListJugador,
        navigateTo = {}
    )
}
