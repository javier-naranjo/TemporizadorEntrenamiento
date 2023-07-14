package com.example.temporizadorentrenamiento.entrenamiento.data

import javax.inject.Inject
import javax.inject.Singleton
import com.example.temporizadorentrenamiento.entrenamiento.ui.model.Tiempos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class tiemposRepository @Inject constructor(private val tiemposDAO: tiemposDAO){

    val tiempo: Flow<List<Tiempos>> = tiemposDAO.getTiempos().map { items -> items.map { Tiempos(numRounds = it.numRounds, round = it.round, descanso = it.descanso) }}

    suspend fun add(Tiempos:Tiempos){
        tiemposDAO.instertTiempos(tiemposEntity(id = Tiempos.id, numRounds = Tiempos.numRounds, round = Tiempos.round, descanso = Tiempos.descanso))
    }

    suspend fun update(Tiempos:Tiempos){
        tiemposDAO.updateTiempos(tiemposEntity(id = Tiempos.id, numRounds = Tiempos.numRounds, round = Tiempos.round, descanso = Tiempos.descanso))
    }
}