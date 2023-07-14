package com.example.temporizadorentrenamiento.entrenamiento.ui

import com.example.temporizadorentrenamiento.entrenamiento.ui.model.Tiempos

sealed interface tiemposUIState {
    object Loading:tiemposUIState
    data class Error(val throwable: Throwable):tiemposUIState
    data class Success(val tiempos:List<Tiempos>):tiemposUIState
}