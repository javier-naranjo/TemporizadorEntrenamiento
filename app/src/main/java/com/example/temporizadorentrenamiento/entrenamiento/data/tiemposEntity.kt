package com.example.temporizadorentrenamiento.entrenamiento.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class tiemposEntity(@PrimaryKey val id:Int, val numRounds:String, val round:String, val descanso:String)