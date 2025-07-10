package edu.ucne.josephcamilo_ap2_p2.presentation.viajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.josephcamilo_ap2_p2.data.remote.Resource
import edu.ucne.josephcamilo_ap2_p2.data.remote.dto.ViajeDto
import edu.ucne.josephcamilo_ap2_p2.data.repository.ViajesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ViajeViewModel @Inject constructor(
    private val viajeRepository: ViajesRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(ViajeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getViajes()
    }

    fun onEvent(event: ViajeEvent) {
        when (event) {
            is ViajeEvent.FechaChange -> fechaChange(event.fecha)
            is ViajeEvent.MillasChange -> millasChange(event.millas)
            is ViajeEvent.TasaDolarChange -> tasaDolarChange(event.tasaDolar)
            is ViajeEvent.MontoChange -> montoChange(event.monto)
            is ViajeEvent.ObservacionesChange -> observacionesChange(event.observaciones)
            ViajeEvent.GetViajes -> getViajes()
            ViajeEvent.LimpiarErrorMessageFecha -> limpiarErrorMessageFecha()
            ViajeEvent.LimpiarErrorMessageMillas -> limpiarErrorMessageMillas()
            ViajeEvent.LimpiarErrorMessageTasaDolar -> limpiarErrorMessageTasaDolar()
            ViajeEvent.LimpiarErrorMessageMonto -> limpiarErrorMessageMonto()
            ViajeEvent.LimpiarErrorMessageObservaciones -> limpiarErrorMessageObservaciones()
            ViajeEvent.Nuevo -> nuevo()
            ViajeEvent.PostViaje -> addViaje()
            is ViajeEvent.ViajeIdChange -> viajeIdChange(event.viajeId)
            ViajeEvent.ResetSuccessMessage -> _uiState.update { it.copy(isSuccess = false, successMessage = null) }
            is ViajeEvent.GetViaje -> findViaje(event.id)
        }
    }

    private fun limpiarErrorMessageFecha() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorFecha = "")
            }
        }
    }

    private fun limpiarErrorMessageMillas() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMillas = "")
            }
        }
    }

    private fun limpiarErrorMessageTasaDolar() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorTasaDolar = "")
            }
        }
    }

            private fun limpiarErrorMessageMonto() {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(errorMonto = "")
                    }
                }
            }

            private fun limpiarErrorMessageObservaciones() {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(errorObservaciones = "")
                    }
                }
            }

    private fun fechaChange(fecha: Date) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(fecha = fecha)
            }
        }
    }
                    private fun millasChange(millas: Double) {
                        viewModelScope.launch {
                            _uiState.update {
                                it.copy(millas = millas)
                            }
                        }
                    }
                    private fun tasaDolarChange(tasaDolar: Double) {
                        viewModelScope.launch {
                            _uiState.update {
                                it.copy(tasaDolar = tasaDolar)
                            }
                        }
                    }
                    private fun montoChange(monto: Double) {
                        viewModelScope.launch {
                            _uiState.update {
                                it.copy(monto = monto)
                            }
                        }
                    }
                    private fun observacionesChange(observaciones: String) {
                        viewModelScope.launch {
                            _uiState.update {
                                it.copy(observaciones = observaciones)
                            }
                        }
                    }

    private fun viajeIdChange(id: Int){
        viewModelScope.launch {
            _uiState.update {
                it.copy(viajeId = id)
            }
        }
    }

    private fun nuevo() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    fecha = Date(),
                    millas = 0.0,
                    tasaDolar = 0.0,
                    monto = 0.0,
                    observaciones = ""
                )
            }
        }
    }

    private fun addViaje() {
        viewModelScope.launch {
            var error = false

            if (_uiState.value.fecha == Date()) {
                _uiState.update {
                    it.copy(errorFecha = "Este campo es obligatorio *")
                }
                error = true
            }
            if (_uiState.value.millas <= 0.0) {
                _uiState.update {
                    it.copy(errorMillas = "Este campo es obligatorio y debe ser mayor que cero *")
                }
                error = true
            }
            if (_uiState.value.tasaDolar <= 0.0) {
                _uiState.update {
                    it.copy(errorTasaDolar = "Este campo es obligatorio y debe ser mayor que cero *")
                }
                error = true
            }
            if(_uiState.value.monto <= 0.0){
                _uiState.update {
                    it.copy(errorMonto = "Este campo es obligatorio y debe ser mayor que cero *")
                }
                error = true
            }
            if (_uiState.value.observaciones.isBlank()) {
                _uiState.update {
                    it.copy(errorObservaciones = "Este campo es obligatorio *")
                }
                error = true
            }
            if (error) return@launch
            try {
                viajeRepository.saveViaje(_uiState.value.toEntity())

                // Actualizar estado con mensaje de éxito
                _uiState.update {
                    it.copy(
                        isSuccess = true,
                        successMessage = "Viaje guardado correctamente",
                        errorMessage = null
                    )
                }

                getViajes()
                nuevo()

                // Navegar de regreso después de un breve retraso para que se vea el mensaje
                delay(2000) // Espera 2 segundos para mostrar el mensaje
                _uiEvent.send(UiEvent.NavigateUp)
            }catch (e: retrofit2.HttpException) {
                if (e.code() == 500) {
                    // Si es un error 500, usa los datos locales y notifica
                    _uiState.update {
                        it.copy(
                            isSuccess = true,
                            successMessage = "Viaje guardado. Falló sincronización con el servidor (500).",
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error en la API: ${e.code()} - ${e.message}",
                            isSuccess = false
                        )
                    }
                    return@launch // Salir si es otro error de API

                }
            }catch (e: Exception){
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al guardar el viaje: ${e.localizedMessage}",
                        isSuccess = false
                    )
                }
            }

            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    fun findViaje(viajeId: Int) {
        viewModelScope.launch {
            if (viajeId > 0) {
                viajeRepository.getViajes(viajeId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val viaje = resource.data?.firstOrNull()
                            _uiState.update {
                                it.copy(
                                    viajeId = viaje?.viajeId ?: 0,
                                    fecha = viaje?.fecha ?: Date(),
                                    millas = viaje?.millas ?: 0.0,
                                    tasaDolar = viaje?.tasaDolar ?: 0.0,
                                    monto = viaje?.monto ?: 0.0,
                                    observaciones = viaje?.observaciones ?: ""
                                )
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(errorMessage = resource.message)
                            }
                        }
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }
                }
            }
        }
    }

    private fun getViajes() {
        viewModelScope.launch {
            viajeRepository.getViaje().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                viajes = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}

fun ViajeUiState.toEntity() = ViajeDto(
    viajeId = viajeId,
    fecha = fecha ?: Date(),
    millas = millas ?: 0.0,
    tasaDolar = tasaDolar ?: 0.0,
    monto = monto ?: 0.0,
    observaciones = observaciones ?: "",
)