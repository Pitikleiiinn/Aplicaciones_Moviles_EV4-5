package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ariztia.bebederos.data.model.Flushing
import cl.ariztia.bebederos.data.model.FlushingRequest
import cl.ariztia.bebederos.data.model.Galpon
import cl.ariztia.bebederos.data.model.Granja
import cl.ariztia.bebederos.data.model.Linea
import cl.ariztia.bebederos.data.repository.FlushingRepository
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Estado completo del formulario "Registrar flushing". */
data class FlushingFormUi(
    // Catálogos para los menús desplegables (se cargan desde ms-granjas)
    val granjas: List<Granja> = emptyList(),
    val galpones: List<Galpon> = emptyList(),
    val lineas: List<Linea> = emptyList(),
    // Selecciones y campos
    val granja: Granja? = null,
    val galpon: Galpon? = null,
    val linea: Linea? = null,
    val responsable: String = "",
    val duracion: String = "",
    val observacion: String = "",
    // Resultado de validación: clave del campo -> mensaje de error
    val errores: Map<String, String> = emptyMap(),
    val enviando: Boolean = false,
    val errorCatalogo: String? = null,
    /** Mensaje puntual para Snackbar (éxito o error del servidor) */
    val mensaje: String? = null
)

/**
 * [VIEWMODEL] Formulario de flushing: menús en cascada Granja → Galpón → Línea,
 * validación de campos y envío a ms-flushing (persistencia remota).
 */
class FlushingFormViewModel(
    private val granjasRepo: GranjasRepository,
    private val flushingRepo: FlushingRepository,
    nombreResponsable: String
) : ViewModel() {

    private val _ui = MutableStateFlow(FlushingFormUi(responsable = nombreResponsable))
    val ui: StateFlow<FlushingFormUi> = _ui.asStateFlow()

    // ----- Carga de catálogos en cascada -----
    fun cargarGranjas() {
        viewModelScope.launch {
            when (val r = granjasRepo.granjas()) {
                is Resultado.Exito -> _ui.value = _ui.value.copy(granjas = r.datos, errorCatalogo = null)
                is Resultado.Error -> _ui.value = _ui.value.copy(errorCatalogo = r.mensaje)
            }
        }
    }

    fun seleccionarGranja(granja: Granja) {
        // Al cambiar de granja se limpian galpón y línea
        _ui.value = _ui.value.copy(granja = granja, galpon = null, linea = null, galpones = emptyList(), lineas = emptyList(),
            errores = _ui.value.errores - "granja")
        viewModelScope.launch {
            (granjasRepo.galpones(granja.id) as? Resultado.Exito)?.let { _ui.value = _ui.value.copy(galpones = it.datos) }
        }
    }

    fun seleccionarGalpon(galpon: Galpon) {
        _ui.value = _ui.value.copy(galpon = galpon, linea = null, lineas = emptyList(), errores = _ui.value.errores - "galpon")
        viewModelScope.launch {
            (granjasRepo.lineas(galpon.id) as? Resultado.Exito)?.let { _ui.value = _ui.value.copy(lineas = it.datos) }
        }
    }

    fun seleccionarLinea(linea: Linea) {
        _ui.value = _ui.value.copy(linea = linea, errores = _ui.value.errores - "linea")
    }

    // ----- Campos de texto -----
    fun cambiarResponsable(v: String) { _ui.value = _ui.value.copy(responsable = v, errores = _ui.value.errores - "responsable") }
    fun cambiarDuracion(v: String) { _ui.value = _ui.value.copy(duracion = v.filter { it.isDigit() }, errores = _ui.value.errores - "duracion") }
    fun cambiarObservacion(v: String) { _ui.value = _ui.value.copy(observacion = v, errores = _ui.value.errores - "observacion") }

    /** [VALIDACIÓN] Devuelve los errores por campo (mapa vacío = formulario válido). Se prueba con test unitario. */
    fun validar(estado: FlushingFormUi = _ui.value): Map<String, String> {
        val e = mutableMapOf<String, String>()
        if (estado.granja == null) e["granja"] = "Selecciona una granja"
        if (estado.galpon == null) e["galpon"] = "Selecciona un galpón"
        if (estado.linea == null) e["linea"] = "Selecciona una línea"
        if (estado.responsable.isBlank()) e["responsable"] = "Ingresa el responsable"
        val minutos = estado.duracion.toIntOrNull()
        if (minutos == null || minutos < 1) e["duracion"] = "Ingresa una duración en minutos (mínimo 1)"
        if (estado.observacion.isBlank()) e["observacion"] = "Ingresa una observación"
        return e
    }

    fun guardar() {
        val errores = validar()
        if (errores.isNotEmpty()) {
            _ui.value = _ui.value.copy(errores = errores, mensaje = "Completa todos los campos para registrar el flushing")
            return
        }
        val s = _ui.value
        val request = FlushingRequest(
            lineaId = s.linea!!.id,
            granjaNombre = s.granja!!.nombre,
            galponNombre = s.galpon!!.nombre,
            lineaNombre = s.linea.nombre,
            responsable = s.responsable.trim(),
            duracionMinutos = s.duracion.toInt(),
            observacion = s.observacion.trim()
        )
        viewModelScope.launch {
            _ui.value = _ui.value.copy(enviando = true)
            when (val r = flushingRepo.crear(request)) {
                is Resultado.Exito -> _ui.value = _ui.value.copy(
                    enviando = false, mensaje = "Flushing registrado correctamente",
                    // se limpian los campos pero se conserva responsable y selección para registrar otro rápido
                    duracion = "", observacion = "", errores = emptyMap()
                )
                is Resultado.Error -> _ui.value = _ui.value.copy(enviando = false, mensaje = r.mensaje)
            }
        }
    }

    fun mensajeMostrado() { _ui.value = _ui.value.copy(mensaje = null) }
}
