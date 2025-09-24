package edu.ucne.TicTacToePlay.tareas.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.TicTacToePlay.tareas.local.dao.JugadorDao
import edu.ucne.TicTacToePlay.tareas.local.database.JugadorDataBase
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import edu.ucne.TicTacToePlay.tareas.local.dao.LogroDao
import edu.ucne.TicTacToePlay.tareas.local.dao.PartidaDao

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideJugadorDb(@ApplicationContext appContext: Context) : JugadorDataBase {
        return Room.databaseBuilder(
            appContext,
            JugadorDataBase::class.java,
            "Jugador.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideTaskDao(db: JugadorDataBase): JugadorDao {
        return db.jugadorDao()
    }
    @Provides
    @Singleton
    fun providePartidaDao(db: JugadorDataBase): PartidaDao {
        return db.partidaDao()
    }
    @Provides
    @Singleton
    fun provideLogroDao(db: JugadorDataBase): LogroDao {
        return db.logroDao()
    }
}
