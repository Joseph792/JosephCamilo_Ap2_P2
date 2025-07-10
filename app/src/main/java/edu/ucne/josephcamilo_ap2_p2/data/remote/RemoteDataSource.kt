package edu.ucne.josephcamilo_ap2_p2.data.remote

import edu.ucne.josephcamilo_ap2_p2.data.remote.dto.ViajeDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val viajesApi: ViajesApi
){
    suspend fun getViajes()= viajesApi.getViajes()

    suspend fun updateViaje(viajeDto: ViajeDto)= viajesApi.updateViaje(viajeDto)

    suspend fun saveViaje(viajeDto: ViajeDto)= viajesApi.saveViaje(viajeDto)

    suspend fun getViaje(id: Int)= viajesApi.getViaje(id)
}