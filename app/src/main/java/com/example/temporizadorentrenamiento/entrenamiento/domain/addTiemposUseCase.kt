package com.example.temporizadorentrenamiento.entrenamiento.domain

import com.example.temporizadorentrenamiento.entrenamiento.data.tiemposRepository
import com.example.temporizadorentrenamiento.entrenamiento.ui.model.Tiempos
import javax.inject.Inject

class addTiemposUseCase @Inject constructor(private val tiemposRepository: tiemposRepository){
    suspend operator fun invoke(Tiempos: Tiempos){
        tiemposRepository.add(Tiempos)
    }
}