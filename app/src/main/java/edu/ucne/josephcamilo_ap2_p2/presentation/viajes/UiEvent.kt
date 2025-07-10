package edu.ucne.josephcamilo_ap2_p2.presentation.viajes

sealed class UiEvent {
    object NavigateUp : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
}