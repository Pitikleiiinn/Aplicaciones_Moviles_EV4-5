
package com.example.miapp_grupox.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miapp_grupox.model.DatabaseProvider
import com.example.miapp_grupox.model.LineBebedero
import com.example.miapp_grupox.model.OpcionesBebedero
import com.example.miapp_grupox.remote.ClimaActual
import com.example.miapp_grupox.remote.Resultado
import com.example.miapp_grupox.repository.BebederoRepository
import com.example.miapp_grupox.repository.ClimaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BebederoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = BebederoRepository(
        DatabaseProvider.getDatabase(application).bebederoDao()
    )

    // Repositorio de la API externa (clima)
    private val climaRepository = ClimaRepository()

    private val _lineas =
        MutableStateFlow<List<LineBebedero>>(emptyList())

    val lineas: StateFlow<List<LineBebedero>> =
        _lineas.asStateFlow()

    // Clima exterior de la granja elegida (null = aún no cargado o no disponible)
    private val _clima = MutableStateFlow<ClimaActual?>(null)
    val clima: StateFlow<ClimaActual?> = _clima.asStateFlow()

    // Mensaje de error de red para mostrar al usuario (null = nada que mostrar)
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        // La pantalla lee siempre de Room, así se ven los datos guardados aunque no haya conexión
        viewModelScope.launch {
            repository.obtenerLineas().collect { datos ->
                _lineas.value = datos
            }
        }

        // En paralelo se sincroniza con el servidor (ms-temperaturas)
        viewModelScope.launch {
            repository.inicializarDatos()?.let { error ->
                _mensaje.value = "$error Se muestran los datos guardados en el teléfono."
            }
        }
    }

    // Registra la temperatura en el servidor; el servidor calcula el estado y la app se actualiza sola
    fun guardarTemperatura(
        granja: String,
        galpon: String,
        linea: String,
        temperatura: Double
    ) {
        viewModelScope.launch {
            repository.guardarTemperatura(granja, galpon, linea, temperatura)?.let { error ->
                _mensaje.value = "No se pudo guardar la temperatura. $error"
            }
        }
    }

    // Consulta el clima de la granja en la API externa
    fun cargarClima(granja: String) {
        val coordenadas = OpcionesBebedero.coordenadas[granja] ?: return
        viewModelScope.launch {
            _clima.value = when (val r = climaRepository.actual(coordenadas.first, coordenadas.second)) {
                is Resultado.Exito -> r.datos
                is Resultado.Error -> null
            }
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }
}
