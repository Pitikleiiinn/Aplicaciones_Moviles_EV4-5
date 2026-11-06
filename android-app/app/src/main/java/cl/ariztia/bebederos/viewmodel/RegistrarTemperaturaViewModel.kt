package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ariztia.bebederos.data.model.Linea
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.data.repository.TemperaturasRepository
import cl.ariztia.bebederos.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Estado del formulario "Registrar temperatura". */
data class TemperaturaFormUi(
    val texto: String = "",
    val error: String? = null,
    val enviando: Boolean = false,
    /** Mensaje de éxito; cuando no es null la pantalla vuelve atrás */
    val exito: String? = null
)

/** [VIEWMODEL] Formulario para registrar la temperatura medida por el operario en una línea. */
class RegistrarTemperaturaViewModel(
    private val granjas: GranjasRepository,
    private val temperaturas: TemperaturasRepository,
    private val lineaId: Long
) : ViewModel() {

    private val _ui = MutableStateFlow(TemperaturaFormUi())
    val ui: StateFlow<TemperaturaFormUi> = _ui.asStateFlow()

    private val _linea = MutableStateFlow<Linea?>(null)
    val linea: StateFlow<Linea?> = _linea.asStateFlow()

    fun cargarLinea() {
        viewModelScope.launch {
            (granjas.linea(lineaId) as? Resultado.Exito)?.let { _linea.value = it.datos }
        }
    }

    fun cambiarTexto(texto: String) {
        _ui.value = _ui.value.copy(texto = texto, error = null)
    }

    /** [VALIDACIÓN] Acepta coma o punto decimal y exige un rango físicamente razonable. */
    fun validar(texto: String): String? {
        val valor = texto.replace(',', '.').toDoubleOrNull() ?: return "Ingresa un número válido (ej. 24.5)"
        if (valor < -10.0 || valor > 80.0) return "La temperatura debe estar entre -10 y 80 °C"
        return null
    }

    fun guardar() {
        val texto = _ui.value.texto
        val error = validar(texto)
        if (error != null) {
            _ui.value = _ui.value.copy(error = error)
            return
        }
        val valor = texto.replace(',', '.').toDouble()
        viewModelScope.launch {
            _ui.value = _ui.value.copy(enviando = true, error = null)
            when (val r = temperaturas.registrar(lineaId, _linea.value?.descripcionCompleta, valor)) {
                is Resultado.Exito -> _ui.value = _ui.value.copy(
                    enviando = false,
                    exito = "Registrado: ${r.datos.temperatura} °C (${r.datos.estado.name})"
                )
                is Resultado.Error -> _ui.value = _ui.value.copy(enviando = false, error = r.mensaje)
            }
        }
    }
}
