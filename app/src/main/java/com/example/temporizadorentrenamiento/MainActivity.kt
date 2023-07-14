package com.example.temporizadorentrenamiento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.temporizadorentrenamiento.entrenamiento.ui.EntrenoViewModel
import com.example.temporizadorentrenamiento.entrenamiento.ui.Principal
import com.example.temporizadorentrenamiento.entrenamiento.ui.SplashScreen
import com.example.temporizadorentrenamiento.entrenamiento.ui.Temporizador
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val EntrenoViewModel:EntrenoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        MobileAds.initialize(this)
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val navigationController = rememberNavController()
                   NavHost(navController = navigationController, startDestination = "Inicio"){
                        composable("Inicio"){ Principal(navigationController, EntrenoViewModel)}
                        composable("Temporizador"){ Temporizador(navigationController, EntrenoViewModel) }
                        composable("Splash"){ SplashScreen(navigationController) }
                   }
                }
            }
        }
    }
}