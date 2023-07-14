package com.example.temporizadorentrenamiento.entrenamiento.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.temporizadorentrenamiento.entrenamiento.ui.model.Tiempos
import com.example.temporizadorentrenamiento.ui.theme.Typography

@Composable
    fun Principal(navigationController: NavHostController, EntrenoViewModel: EntrenoViewModel) {
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        val UIState by produceState<tiemposUIState>(initialValue = tiemposUIState.Loading, key1 = lifecycle, key2= EntrenoViewModel){
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED){
                EntrenoViewModel.UIState.collect{value = it}
            }
        }
        when(UIState){
            is tiemposUIState.Error -> {}
            is tiemposUIState.Loading -> {}
            is tiemposUIState.Success -> {
                EntrenoViewModel.Parar()
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)) {
                    Cabecera(Modifier.align(Alignment.TopCenter))
                    Cuerpo(Modifier.align(Alignment.Center), navigationController, EntrenoViewModel, (UIState as tiemposUIState.Success).tiempos)
                    Pie(Modifier.align(Alignment.BottomCenter))
                }
            }
        }
    }

    @Composable
    fun Cabecera(modifier: Modifier){
        Row(modifier = modifier.padding(vertical = 16.dp)){
            Text("ENTRENAMIENTO", style = Typography.titleLarge)
        }
    }

    @Composable
    fun Cuerpo(
        modifier: Modifier,
        navigationController: NavHostController,
        EntrenoViewModel: EntrenoViewModel,
        settings:List<Tiempos>
    ){
        val t_round by EntrenoViewModel.round.observeAsState("00:00")
        val t_rest by EntrenoViewModel.descanso.observeAsState("00:00")
        val rounds by EntrenoViewModel.numRounds.observeAsState(0)
        settings.forEach{
            EntrenoViewModel.Actualizar(nr = it.numRounds, r = it.round, d = it.descanso)
        }
        Column(modifier = modifier) {
            Spacer(modifier = modifier.size(48.dp))
            Row(Modifier.height(96.dp)) {
                Icon(imageVector = Icons.Filled.SportsMma, contentDescription = "Tiempo por round",
                    Modifier
                        .weight(1.0f)
                        .fillMaxSize(0.75f)
                        .align(Alignment.CenterVertically))
                Column(
                    Modifier
                        .align(Alignment.CenterVertically)
                        .weight(2.0f)) {
                    Text("Tiempo por round:", style = Typography.bodySmall)
                    Text(t_round, style = Typography.bodyMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                Row(Modifier.weight(1.0f)) {
                    IconButton(onClick = {
                        EntrenoViewModel.AjustarTiempo(periodo = 10, cadena = t_round, round = true)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Disminuir tiempo de round",
                            Modifier
                                .fillMaxSize(0.85f)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        EntrenoViewModel.AjustarTiempo(periodo = -10, cadena = t_round, round = true)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DoNotDisturbOn,
                            contentDescription = "Aumentar tiempo de round",
                            Modifier
                                .fillMaxSize(0.85f)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Divider(Modifier.padding(16.dp))
            Row(Modifier.height(96.dp)) {
                Icon(imageVector = Icons.Filled.Snooze, contentDescription = "Descanso entre rounds",
                    Modifier
                        .fillMaxSize(0.75f)
                        .weight(1.0f)
                        .align(Alignment.CenterVertically))
                Column(
                    Modifier
                        .align(Alignment.CenterVertically)
                        .weight(2.0f)) {
                    Text("Descanso entre rounds:", style = Typography.bodySmall)
                    Text(t_rest, style = Typography.bodyMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                Row(Modifier.weight(1.0f)) {
                    IconButton(onClick = {
                        EntrenoViewModel.AjustarTiempo(periodo = 10, cadena = t_rest, round = false)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Disminuir tiempo de descanso",
                            Modifier
                                .fillMaxSize(0.85f)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        EntrenoViewModel.AjustarTiempo(periodo = -10, cadena = t_rest, round = false)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DoNotDisturbOn,
                            contentDescription = "Aumentar tiempo de descanso",
                            Modifier
                                .fillMaxSize(0.85f)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Divider(Modifier.padding(16.dp))
            Row(Modifier.height(96.dp)) {
                Icon(imageVector = Icons.Filled.HourglassEmpty, contentDescription = "Número de rounds",
                    Modifier
                        .fillMaxSize(0.75f)
                        .weight(1.0f)
                        .align(Alignment.CenterVertically))
                Column(
                    Modifier
                        .align(Alignment.CenterVertically)
                        .weight(2.0f)) {
                    Text("Numero de rounds:", style = Typography.bodySmall)
                    Text(rounds.toString(), style = Typography.bodyMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                Row (Modifier.weight(1.0f)){
                    IconButton(onClick = {EntrenoViewModel.ModificarNumRounds(1)}) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Aumentar número de rounds",
                            Modifier.fillMaxSize(0.85f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        EntrenoViewModel.ModificarNumRounds(-1)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DoNotDisturbOn,
                            contentDescription = "Disminuir número de rounds",
                            Modifier
                                .fillMaxSize(0.85f)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Spacer(Modifier.size(96.dp))
            Box(
                Modifier
                    .size(154.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        EntrenoViewModel.ComprobarMenu(navigationController)
                    }) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Empezar!",
                    Modifier
                        .fillMaxSize(0.5f)
                        .align(
                            Alignment.Center
                        ))
            }
        }
    }

    @Composable
    fun Pie(modifier: Modifier){
        Row(modifier = modifier) {

        }
    }