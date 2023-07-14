package com.example.temporizadorentrenamiento.entrenamiento.domain

import com.example.temporizadorentrenamiento.entrenamiento.data.tiemposRepository
import com.example.temporizadorentrenamiento.entrenamiento.ui.model.Tiempos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class getTiemposUseCase @Inject constructor(private val tiemposRepository: tiemposRepository) {
    operator fun invoke():Flow<List<Tiempos>>{
        return tiemposRepository.tiempo
    }
}