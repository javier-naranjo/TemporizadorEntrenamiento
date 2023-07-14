package com.example.temporizadorentrenamiento.entrenamiento.ui

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.temporizadorentrenamiento.R
import com.example.temporizadorentrenamiento.entrenamiento.domain.addTiemposUseCase
import com.example.temporizadorentrenamiento.entrenamiento.domain.getTiemposUseCase
import com.example.temporizadorentrenamiento.entrenamiento.domain.updateTiemposUseCase
import com.example.temporizadorentrenamiento.entrenamiento.ui.model.Tiempos
import com.example.temporizadorentrenamiento.entrenamiento.ui.tiemposUIState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntrenoViewModel@Inject constructor(
    private val addTiemposUseCase: addTiemposUseCase
, private val updateTiemposUseCase: updateTiemposUseCase
, getTiemposUseCase: getTiemposUseCase
):ViewModel() {

    val UIState:StateFlow<tiemposUIState> = getTiemposUseCase().map(::Success)
        .catch { tiemposUIState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), tiemposUIState.Loading)

    /////////////////////////////////////////////////////////////////////

    private val _numRounds = MutableLiveData<Int>()
    val numRounds:LiveData<Int> = _numRounds

    private val _descanso = MutableLiveData<String>()
    val descanso:LiveData<String> = _descanso

    private val _round = MutableLiveData<String>()
    val round:LiveData<String> = _round

    private var recuperado:Boolean = true

    ////////////////////////////////////////////////////////////////////////////////////

    private val _tiempoRestante = MutableLiveData<String>()
    val tiempoRestante:LiveData<String> = _tiempoRestante

    private val _etapa = MutableLiveData<String>()
    val etapa:LiveData<String> = _etapa

    private val _titulo = MutableLiveData<String>()
    val titulo:LiveData<String> = _titulo

    private val _pausado = MutableLiveData<Boolean>()
    val pausado:LiveData<Boolean> = _pausado

    private val _angulo = MutableLiveData<Float>()
    val angulo:LiveData<Float> = _angulo

    ////////////////////////////////////////////////////////////////////////////////////
    fun Guardar(nr:String, r:String, d:String){
        Log.d("LOGNR_guardado", nr)
        Log.d("LOGR_guardado", r)
        Log.d("LOGD_guardado", d)
        viewModelScope.launch {
                addTiemposUseCase(Tiempos(id = 1, numRounds= nr, round = r, descanso = d))
        }
    }

    fun Actualizar(nr:String, r:String, d:String){
        if(recuperado){
            _numRounds.value = nr.toInt()?:0
            _round.value = r?:"00:00"
            _descanso.value = d?:"00:00"
        }
        recuperado = false
    }

    fun ModificarNumRounds(x:Int){
        val temp:Int = _numRounds.value ?: 0
        _numRounds.value = x + temp
        if (_numRounds.value!! < 0) _numRounds.value = 0
        Guardar(nr = _numRounds.value.toString(), r = _round.value ?: "00:00", d = _descanso.value ?: "00:00")
    }

    fun ComprobarMenu(navigationController: NavHostController) {
        if(_numRounds.value != null && _descanso.value != null && _round.value != null) {
            if (_numRounds.value!! > 0 && aEntero(_descanso.value!!) > 0 && aEntero(_round.value!!) > 0)
                navigationController.navigate("Temporizador")
        }
    }

    fun AjustarTiempo(periodo: Int, cadena: String, round:Boolean){
        var segundos = cadena.substring(3).toInt()
        var minutos = cadena.substring(0, 2).toInt()
        var modificada = ""
        segundos += periodo
        if (segundos >= 60){
            segundos = 0
            minutos++
        }
        if (minutos >= 99){
            segundos = 0
            minutos = 0
        }
        if (segundos < 0){
            segundos = 60 + periodo
            minutos --
        }
        if (minutos < 0){
            segundos = 0
            minutos = 0
        }
        if (minutos < 10)
            modificada = "0" + minutos.toString() + ":"
        else
            modificada = minutos.toString() + ":"
        if (segundos < 10)
            modificada += "0" + segundos.toString()
        else
            modificada += segundos.toString()
        if (round)      _round.value = modificada
        else            _descanso.value = modificada
        val xRounds = _numRounds.value ?: 0
        Guardar(nr = xRounds.toString(), r = _round.value?:"00:00", d = _descanso.value?:"00:00")
    }

    ////////////////////////////////////////////////////////////////////////////////////
    private var rutina: Job? = null
    private var pausar = false
    private var inicio = true
    private var segundosRestantes:Int = 0
    private var etapaActual:Int = 0
    @SuppressLint("StaticFieldLeak")
    private var contexto:Context? = null

    fun playSound() {
        val mediaPlayer = MediaPlayer.create(contexto, R.raw.bell)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mp -> mp.release() }
    }

    fun Iniciar(contxt: Context){
        if(inicio){
            segundosRestantes = 4
            contexto = contxt
            Temporizar( )
            inicio = false
        }
    }

    fun Parar(){
        _numRounds.value = 0
        _round.value = "00:00"
        _descanso.value = "00:00"
        recuperado = true
        _pausado.value = true
        pausar = false
        inicio = true
        etapaActual = 0
        segundosRestantes = 0
        rutina?.cancel()
    }

    fun PausarReanudar(){
        pausar = !pausar
        _pausado.value = pausar
        if(pausar){
            rutina?.cancel()
        }else{
            Temporizar()
        }
    }

    fun aEntero(x:String): Int{
        return (x.substring( 0, x.indexOf(":")).toInt() * 60) + x.substring(x.indexOf(":") + 1).toInt()
    }

    fun aCadena(x:Int): String{
        var resultado = "00:00"
        if(x / 60 < 10)      resultado = "0" + (x / 60).toString() + ":" + (x % 60).toString()
        if (x % 60 < 10)     resultado = (x / 60).toString() + ":0" + (x % 60).toString()
        if (x % 60 < 10 && x / 60 < 10)    resultado = "0" +  (x / 60).toString() + ":0" + (x % 60).toString()
        return resultado
    }

    fun Temporizar() {
        rutina = viewModelScope.launch {
            while(true){
                segundosRestantes --
                delay(1000)
                if (segundosRestantes < 0){
                    if(etapaActual % 2 == 0){
                        segundosRestantes = aEntero(_round.value!!)
                        _titulo.value = "Fight!"
                    }
                    else{
                        segundosRestantes = aEntero(_descanso.value!!)
                        _titulo.value = "Descanso"
                    }
                    playSound()
                    if(etapaActual == _numRounds.value!!)  this.cancel()
                    etapaActual++
                    _etapa.value = etapaActual.toString()
                }
                _tiempoRestante.value = aCadena(segundosRestantes)
                _angulo.value = segundosRestantes.toFloat() * 360f / aEntero(_round.value?:"00:00").toFloat()
            }
        }
    }
}