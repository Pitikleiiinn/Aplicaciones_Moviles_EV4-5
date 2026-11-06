package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ariztia.bebederos.data.model.Lectura
import cl.ariztia.bebederos.data.model.Linea
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.data.repository.TemperaturasRepository
import cl.ariztia.bebederos.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Una línea junto a su última lectura de temperatura (puede ser null si aún no tiene). */
data class LineaConLectura(val linea: Linea, val lectura: Lectura?)

/**
 * [VIEWMODEL] Nivel 3: líneas de un galpón con su temperatura actual.
 * Combina DOS microservicios: ms-granjas (las líneas) + ms-temperaturas (última lectura de cada una).
 */
class LineasViewModel(
    private val granjas: GranjasRepository,
    private val temperaturas: TemperaturasRepository,
    private val galponId: Long
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<LineaConLectura>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<LineaConLectura>>> = _estado.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            when (val lineas = granjas.lineas(galponId)) {
                is Resultado.Error -> _estado.value = UiState.Error(lineas.mensaje)
                is Resultado.Exito -> {
                    // Si falla ms-temperaturas igual mostramos las líneas (sin temperatura)
                    val lecturas = if (lineas.datos.isEmpty()) emptyMap() else
                        (temperaturas.ultimas(lineas.datos.map { it.id }) as? Resultado.Exito)
                            ?.datos.orEmpty().associateBy { it.lineaId }
                    _estado.value = UiState.Exito(lineas.datos.map { LineaConLectura(it, lecturas[it.id]) })
                }
            }
        }
    }
}
