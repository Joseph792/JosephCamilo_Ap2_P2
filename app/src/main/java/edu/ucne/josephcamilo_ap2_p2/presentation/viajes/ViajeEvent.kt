package edu.ucne.josephcamilo_ap2_p2.presentation.viajes

import java.util.Date

sealed interface ViajeEvent{
        data class ViajeIdChange(val viajeId: Int): ViajeEvent
        data class FechaChange(val fecha: Date): ViajeEvent
        data class MillasChange(val millas: Double): ViajeEvent
        data class TasaDolarChange(val tasaDolar: Double): ViajeEvent
        data class MontoChange(val monto: Double): ViajeEvent
        data class ObservacionesChange(val observaciones: String): ViajeEvent

        data object PostViaje: ViajeEvent
        data object GetViajes: ViajeEvent
        data object Nuevo: ViajeEvent
        data object LimpiarErrorMessageFecha: ViajeEvent
        data object LimpiarErrorMessageMillas: ViajeEvent
        data object LimpiarErrorMessageTasaDolar: ViajeEvent
        data object LimpiarErrorMessageMonto: ViajeEvent
        data object LimpiarErrorMessageObservaciones: ViajeEvent
        data class GetViaje(val id: Int): ViajeEvent
        data object ResetSuccessMessage: ViajeEvent
}