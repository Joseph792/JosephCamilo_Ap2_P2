package edu.ucne.josephcamilo_ap2_p2.presentation.viajes

import edu.ucne.josephcamilo_ap2_p2.data.remote.dto.ViajeDto
import java.util.Date

data class ViajeUiState (
    val viajeId: Int? = null,
    val fecha: Date = Date(),
    val millas: Double = 0.0,
    val tasaDolar: Double = 0.0 ,
    val monto: Double = 0.0,
    val observaciones: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorFecha: String? = null,
    val errorMillas: String? = null,
    val errorTasaDolar: String? = null,
    val errorMonto: String? = null,
    val errorObservaciones: String? = null,
    val successMessage: String? = null,
    val viajes: List<ViajeDto> = emptyList()
)