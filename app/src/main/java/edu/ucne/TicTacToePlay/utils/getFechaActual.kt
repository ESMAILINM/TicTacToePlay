package edu.ucne.TicTacToePlay.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFechaActual(): String {
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formato.format(Date())
}
