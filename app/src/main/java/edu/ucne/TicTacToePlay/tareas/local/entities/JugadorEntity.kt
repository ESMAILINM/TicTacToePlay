package edu.ucne.TicTacToePlay.tareas.local.entities

import android.provider.ContactsContract
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Jugadores")
data class JugadorEntity(
    @PrimaryKey val jugadorId: String = UUID.randomUUID().toString(),
    val nombres: String,
    val email: String,
    val remoteId: Int? = null,
    val isPendingCreate: Boolean = false
)
