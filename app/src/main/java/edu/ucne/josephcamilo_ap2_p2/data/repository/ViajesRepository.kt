package edu.ucne.josephcamilo_ap2_p2.data.repository

import edu.ucne.josephcamilo_ap2_p2.data.remote.RemoteDataSource
import edu.ucne.josephcamilo_ap2_p2.data.remote.Resource
import edu.ucne.josephcamilo_ap2_p2.data.remote.dto.ViajeDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ViajesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
    ){
    fun getViajes(viajeId: Int): Flow<Resource<List<ViajeDto>>> = flow {
    try {
        emit(Resource.Loading())
        val viaje = remoteDataSource.getViaje(viajeId)
        emit(Resource.Success(viaje))
    } catch (e: HttpException) {
        emit(Resource.Error("Error de internet: ${e.message()}"))
    } catch (e: Exception) {
        emit(Resource.Error("Error desconocido: ${e.message}"))
    }
}
    suspend fun saveViaje(viajeDto: ViajeDto) = remoteDataSource.saveViaje(viajeDto)

    fun getViaje(): Flow<Resource<List<ViajeDto>>> = flow {
        try {
            emit(Resource.Loading())
            val viaje = remoteDataSource.getViajes()
            emit(Resource.Success(viaje))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }
}