package com.example.temporizadorentrenamiento.entrenamiento.data.di

import android.content.Context
import androidx.room.Room
import com.example.temporizadorentrenamiento.entrenamiento.data.settingsDatabase
import com.example.temporizadorentrenamiento.entrenamiento.data.tiemposDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class databaseModule{
    @Provides
    fun provideDao(settingsDatabase: settingsDatabase):tiemposDAO{
        return settingsDatabase.TiemposDao()
    }

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext appContext:Context):settingsDatabase{
        return Room.databaseBuilder(appContext, settingsDatabase::class.java, "settingsDatabase").build()
    }

}