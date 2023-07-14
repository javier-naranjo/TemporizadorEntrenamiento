package com.example.temporizadorentrenamiento.entrenamiento.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface tiemposDAO {
    @Query("SELECT * from tiemposEntity")
    fun getTiempos(): Flow<List<tiemposEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun instertTiempos(item:tiemposEntity)

    @Update
    suspend fun updateTiempos(item:tiemposEntity)
}