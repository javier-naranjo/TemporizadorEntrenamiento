package com.example.temporizadorentrenamiento.entrenamiento.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [tiemposEntity::class], version = 1)
abstract class settingsDatabase:RoomDatabase() {
    abstract fun TiemposDao():tiemposDAO
}