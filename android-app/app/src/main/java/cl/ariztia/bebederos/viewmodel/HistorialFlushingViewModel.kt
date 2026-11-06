package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ariztia.bebederos.data.model.Flushing
import cl.ariztia.bebederos.data.model.FlushingRequest
import cl.ariztia.bebederos.data.repository.FlushingRepository
import cl.ariztia.bebederos.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** [VIEWMODEL] Historial de flushing con las operaciones de leer, corregir (UPDATE) y eliminar (DELETE). */
class HistorialFlushingViewModel(private val repo: FlushingRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Flushing>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Flushing>>> = _estado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            _estado.value = when (val r = repo.listar()) {
                is Resultado.Exito -> UiState.Exito(r.datos)
                is Resultado.Error -> UiState.Error(r.mensaje)
            }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            when (val r = repo.eliminar(id)) {
                is Resultado.Exito -> { _mensaje.value = "Flushing eliminado"; cargar() }
                is Resultado.Error -> _mensaje.value = r.mensaje
            }
        }
    }

    /** Corrige duración y observación de un flushing ya registrado. */
    fun corregir(original: Flushing, duracion: Int, observacion: String) {
        if (duracion < 1 || observacion.isBlank()) {
            _mensaje.value = "Duración (mínimo 1) y observación son obligatorias"
            return
        }
        val request = FlushingRequest(original.lineaId, original.granjaNombre, original.galponNombre, original.lineaNombre,
            original.responsable, duracion, observacion.trim())
        viewModelScope.launch {
            when (val r = repo.actualizar(original.id, request)) {
                is Resultado.Exito -> { _mensaje.value = "Flushing actualizado"; cargar() }
                is Resultado.Error -> _mensaje.value = r.mensaje
            }
        }
    }

    fun mensajeMostrado() { _mensaje.value = null }
}
