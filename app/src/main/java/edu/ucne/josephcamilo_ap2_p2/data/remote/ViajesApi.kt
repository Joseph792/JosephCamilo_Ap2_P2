package edu.ucne.josephcamilo_ap2_p2.data.remote

import edu.ucne.josephcamilo_ap2_p2.data.remote.dto.ViajeDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ViajesApi {
    @GET("api/Viajes")
    suspend fun getViajes(): List<ViajeDto>

    @GET("api/Viajes/{id}")
    suspend fun getViaje(@Path("id") id: Int): List<ViajeDto>

    @POST("api/Viajes")
    suspend fun saveViaje(@Body usuarioDto: ViajeDto): ViajeDto

    @PUT("api/Viajes/{id}")
    suspend fun updateViaje(@Body usuarioDto: ViajeDto): ViajeDto
}