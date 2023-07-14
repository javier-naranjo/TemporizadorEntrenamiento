package com.example.temporizadorentrenamiento.entrenamiento.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.temporizadorentrenamiento.ui.theme.Typography
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView


@Composable
fun KeepScreenOn() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = context.findActivity()?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun Contenido(navigationController: NavHostController,
              EntrenoViewModel: EntrenoViewModel){
    Box(
        Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp, horizontal = 16.dp)) {
        val contexto = LocalContext.current
        val tiempoRestante by EntrenoViewModel.tiempoRestante.observeAsState("00:04")
        val angulo by EntrenoViewModel.angulo.observeAsState(0f)
        val titulo by EntrenoViewModel.titulo.observeAsState("Get Ready!")
        val etapa by EntrenoViewModel.etapa.observeAsState(0)
        val pausado by EntrenoViewModel.pausado.observeAsState(false)
        EntrenoViewModel.Iniciar(contexto)
        Column(modifier = Modifier.fillMaxSize()){
            Row(modifier = Modifier
                .weight(1.0f)
                .padding(vertical = 16.dp)){
                Text(text = titulo, style = Typography.bodyMedium, modifier = Modifier
                    .weight(1.0f)
                    .padding(horizontal = 32.dp), textAlign = TextAlign.Start)
                Text(text = etapa.toString() + " / 6", style = Typography.bodyMedium, modifier = Modifier
                    .weight(1.0f)
                    .padding(horizontal = 32.dp), textAlign = TextAlign.End)
            }
            Box(modifier = Modifier
                .weight(2.0f)
                .fillMaxSize()){
                CircularSpeedIndicator(angulo)
                Text(text = tiempoRestante, style = Typography.titleMedium, modifier = Modifier.align(Alignment.Center))
            }
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1.0f)
                .padding(vertical = 16.dp)
                .size(72.dp)
                .clickable {
                    EntrenoViewModel.PausarReanudar()
                }){
                if(pausado){
                    Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "Reanudar", modifier = Modifier.fillMaxSize())
                }else{
                    Icon(imageVector = Icons.Filled.PauseCircle, contentDescription = "Pausar", modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Temporizador(
    navigationController: NavHostController,
    EntrenoViewModel: EntrenoViewModel
) {
    KeepScreenOn()
    DisposableEffect(Unit) {
        onDispose {
            EntrenoViewModel.Parar()
        }
    }
    val showConfirmationDialog = remember { mutableStateOf(false) }
    BackHandler {
        showConfirmationDialog.value = true
    }

    if (showConfirmationDialog.value) {
        ConfirmationDialog(
            onConfirm = {
                showConfirmationDialog.value = false
                navigationController.navigateUp() // Navigate back
            },
            onDismiss = {
                showConfirmationDialog.value = false
            }
        )
    }
        Scaffold(bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AndroidView(modifier = Modifier.fillMaxWidth(), factory = {
                    AdView(it).apply {
                        setAdSize(com.google.android.gms.ads.AdSize.BANNER)
                        adUnitId = "ca-app-pub-3940256099942544/6300978111"
                        loadAd(AdRequest.Builder().build())
                    }
                })
            }
        }, content = {
            Contenido(
                navigationController = navigationController,
                EntrenoViewModel = EntrenoViewModel
            )
        })
}

@Composable
fun CircularSpeedIndicator(sweep: Float) {
    val secundario:Color = colorScheme.secondary
    val primario:Color = colorScheme.primary
    val gradiente = Brush.linearGradient(
        colors = listOf(primario, colorScheme.onSurface),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        for(x in (0..360) step 6) {
            rotate(x.toFloat()) {
                drawLine(
                    color = secundario,
                    start = if(x % 5 == 0)
                        Offset(size.width / 10f, size.height / 2f)
                    else
                        Offset(size.width / 20f, size.height / 2f) ,
                    end = Offset(0f, size.height / 2f),
                    8f,
                    StrokeCap.Round
                )
            }
        }
        drawArc(
            brush = gradiente,
            startAngle = 270f,
            sweepAngle = sweep,
            useCenter = false,
            size = size,
            style = Stroke(width = size.width / 20f, cap = StrokeCap.Round)
        )
        drawArc(
            color = primario.copy(alpha = 0.5f),
            startAngle = 270f,
            sweepAngle = sweep,
            useCenter = false,
            size = size,
            style = Stroke(width = size.width / 15f, cap = StrokeCap.Round)
        )
        drawArc(
            color = primario. copy(alpha = 0.15f),
            startAngle = 270f,
            sweepAngle = sweep,
            useCenter = false,
            size = size,
            style = Stroke(width = size.width / 10f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Salir al menú") },
        text = { Text(text = "Regresar al menú detendrá el temporizador, el progreso se perderá. Seguro quieres regresar al menú?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        }
    )
}