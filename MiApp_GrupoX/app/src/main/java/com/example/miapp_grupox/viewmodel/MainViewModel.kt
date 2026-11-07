
package com.example.miapp_grupox.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miapp_grupox.model.DatabaseProvider
import com.example.miapp_grupox.model.FlusingEntity
import com.example.miapp_grupox.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = MainRepository(
        DatabaseProvider.getDatabase(application).flusingDao()
    )

    private val _registros =
        MutableStateFlow<List<FlusingEntity>>(emptyList())

    val registros: StateFlow<List<FlusingEntity>> =
        _registros.asStateFlow()

    // Mensaje de error de red para mostrar al usuario (null = nada que mostrar)
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        // La pantalla lee siempre de Room (copia local del historial)
        viewModelScope.launch {
            repository.obtenerRegistros().collect { datos ->
                _registros.value = datos
            }
        }

        // En paralelo se sincroniza con el servidor (ms-flushing)
        viewModelScope.launch {
            repository.sincronizar()?.let { error ->
                _mensaje.value = "$error Se muestra el historial guardado en el teléfono."
            }
        }
    }

    fun guardarRegistro(registro: FlusingEntity) {
        viewModelScope.launch {
            repository.guardarRegistro(registro)?.let { error ->
                _mensaje.value = "No se pudo guardar el flushing. $error"
            }
        }
    }

    // Corrige solo la observación de un registro ya guardado
    fun actualizarObservacion(registro: FlusingEntity, nuevaObservacion: String) {
        viewModelScope.launch {
            repository.actualizarRegistro(
                registro.copy(observaciones = nuevaObservacion.trim())
            )?.let { error ->
                _mensaje.value = "No se pudo actualizar la observación. $error"
            }
        }
    }

    fun eliminarRegistro(id: Int) {
        viewModelScope.launch {
            repository.eliminarRegistro(id)?.let { error ->
                _mensaje.value = "No se pudo eliminar el registro. $error"
            }
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }
}
