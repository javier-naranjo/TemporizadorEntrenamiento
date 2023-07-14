package com.example.temporizadorentrenamiento.entrenamiento.ui.model

data class Tiempos(val id:Int =System.currentTimeMillis().hashCode(), val numRounds:String, val round:String, val descanso:String)
