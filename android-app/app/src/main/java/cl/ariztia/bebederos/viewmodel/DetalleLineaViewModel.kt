package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ariztia.bebederos.data.model.Alerta
import cl.ariztia.bebederos.data.model.ClimaActual
import cl.ariztia.bebederos.data.model.Lectura
import cl.ariztia.bebederos.data.model.Linea
import cl.ariztia.bebederos.data.repository.ClimaRepository
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.data.repository.TemperaturasRepository
import cl.ariztia.bebederos.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Todo lo que muestra la pantalla de detalle de una línea. */
data class DetalleLinea(
    val linea: Linea,
    val historial: List<Lectura>,
    val alertas: List<Alerta>,
    /** Clima exterior (API externa). Null si la API externa falló: la pantalla igual funciona. */
    val clima: ClimaActual?
) {
    val ultima: Lectura? get() = historial.firstOrNull()
}

/**
 * [VIEWMODEL] Detalle de una línea. Reúne 3 fuentes:
 *   ms-granjas (línea y coordenadas) + ms-temperaturas (historial, alertas) + API externa (clima).
 */
class DetalleLineaViewModel(
    private val granjas: GranjasRepository,
    private val temperaturas: TemperaturasRepository,
    private val clima: ClimaRepository,
    private val lineaId: Long
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<DetalleLinea>>(UiState.Cargando)
    val estado: StateFlow<UiState<DetalleLinea>> = _estado.asStateFlow()

    /** Mensaje puntual (ej. "Lectura eliminada") que la pantalla muestra en un Snackbar */
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            val linea = when (val r = granjas.linea(lineaId)) {
                is Resultado.Exito -> r.datos
                is Resultado.Error -> { _estado.value = UiState.Error(r.mensaje); return@launch }
            }
            val historial = when (val r = temperaturas.historial(lineaId)) {
                is Resultado.Exito -> r.datos
                is Resultado.Error -> { _estado.value = UiState.Error(r.mensaje); return@launch }
            }
            val alertas = (temperaturas.alertasDeLinea(lineaId) as? Resultado.Exito)?.datos.orEmpty()

            // API EXTERNA: se necesitan las coordenadas de la granja de esta línea
            val granja = linea.granjaId?.let { (granjas.granja(it) as? Resultado.Exito)?.datos }
            val climaActual = if (granja?.latitud != null && granja.longitud != null)
                (clima.actual(granja.latitud, granja.longitud) as? Resultado.Exito)?.datos else null

            _estado.value = UiState.Exito(DetalleLinea(linea, historial, alertas, climaActual))
        }
    }

    fun eliminarLectura(id: Long) {
        viewModelScope.launch {
            when (val r = temperaturas.eliminar(id)) {
                is Resultado.Exito -> { _mensaje.value = "Lectura eliminada"; cargar() }
                is Resultado.Error -> _mensaje.value = r.mensaje
            }
        }
    }

    fun mensajeMostrado() { _mensaje.value = null }
}
