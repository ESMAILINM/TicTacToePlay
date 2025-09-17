package edu.ucne.TicTacToePlay.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.TicTacToePlay.data.repository.PartidaRepositoryImpl
import edu.ucne.TicTacToePlay.domain.repository.JugadorRepository
import edu.ucne.TicTacToePlay.domain.repository.PartidaRepository
import edu.ucne.TicTacToePlay.tareas.repository.JugadorRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindJugadorRepository(
        jugadorRepositoryImpl: JugadorRepositoryImpl
    ): JugadorRepository

    @Binds
    @Singleton
    abstract fun bindPartidaRepository(
        partidaRepositoryImpl: PartidaRepositoryImpl
    ): PartidaRepository

}
