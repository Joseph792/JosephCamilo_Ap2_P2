package edu.ucne.josephcamilo_ap2_p2.data.remote.dto

import com.squareup.moshi.Json
import java.util.Date

data class ViajeDto(
    @Json(name = "viajeId") val viajeId: Int?,
    @Json(name = "fecha") val fecha: Date,
    @Json(name = "millas") val millas: Double,
    @Json(name = "tasaDolar") val tasaDolar: Double,
    @Json(name = "monto") val monto: Double,
    @Json(name = "observaciones") val observaciones: String
)