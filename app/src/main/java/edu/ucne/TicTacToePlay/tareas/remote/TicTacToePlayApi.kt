package edu.ucne.TicTacToePlay.tareas.remote

import edu.ucne.TicTacToePlay.tareas.remote.dto.MovimientoDto
import edu.ucne.TicTacToePlay.tareas.remote.dto.PartidaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicTacToePlayApi {

    @GET("api/Movimientos/{id}")
    suspend fun getMovimientos(@Path("id") id: Int): List<MovimientoDto>

    @POST("api/Movimientos")
    suspend fun postMovimiento(@Body movimiento: MovimientoDto): Response<Unit>

    @POST("api/Partidas")
    suspend fun postPartida(@Body partida: PartidaDto): Response<PartidaDto>

}