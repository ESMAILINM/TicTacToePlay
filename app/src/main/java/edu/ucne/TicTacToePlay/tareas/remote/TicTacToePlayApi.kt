package edu.ucne.TicTacToePlay.tareas.remote

import edu.ucne.TicTacToePlay.tareas.remote.dto.JugadorRequest
import edu.ucne.TicTacToePlay.tareas.remote.dto.JugadorResponse
import edu.ucne.TicTacToePlay.tareas.remote.dto.MovimientoDto
import edu.ucne.TicTacToePlay.tareas.remote.dto.PartidaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TicTacToePlayApi {

    @GET("api/Movimientos/{id}")
    suspend fun getMovimientos(@Path("id") id: Int): List<MovimientoDto>

    @POST("api/Movimientos")
    suspend fun postMovimiento(@Body movimiento: MovimientoDto): Response<Unit>

    @POST("api/Partidas")
    suspend fun postPartida(@Body partida: PartidaDto): Response<PartidaDto>

    //Jugador APIs
    @POST("api/Jugadores")
    suspend fun createJugador(@Body request: JugadorRequest): Response<JugadorResponse>

    @PUT("api/Jugadores/{id}")
    suspend fun updateJugador( @Path("id") id : Int, @Body request: JugadorRequest): Response<Unit>

    @GET("api/Jugadores")
    suspend fun getJugadoresFromApi(): Response<List<JugadorResponse>>

    @DELETE("api/Jugadores/{id}")
    suspend fun deleteJugador(@Path("id") id: Int): Response<Unit>


}