package edu.ucne.TicTacToePlay.tareas.local.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
import edu.ucne.TicTacToePlay.tareas.remote.TicTacToePlayApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    private const val BASE_URL = "https://gestionhuacalesapi.azurewebsites.net/"

    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi. Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesTicTacToePlayApi(moshi: Moshi): TicTacToePlayApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TicTacToePlayApi :: class.java)
    }
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
