package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ariztia.bebederos.data.model.Alerta
import cl.ariztia.bebederos.data.repository.TemperaturasRepository
import cl.ariztia.bebederos.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** [VIEWMODEL] Alertas activas (las genera ms-temperaturas cuando una lectura es ADVERTENCIA o CRITICO). */
class AlertasViewModel(private val repo: TemperaturasRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Alerta>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Alerta>>> = _estado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            _estado.value = when (val r = repo.alertasActivas()) {
                is Resultado.Exito -> UiState.Exito(r.datos)
                is Resultado.Error -> UiState.Error(r.mensaje)
            }
        }
    }

    fun atender(id: Long) {
        viewModelScope.launch {
            when (val r = repo.atender(id)) {
                is Resultado.Exito -> { _mensaje.value = "Alerta marcada como atendida"; cargar() }
                is Resultado.Error -> _mensaje.value = r.mensaje
            }
        }
    }

    fun mensajeMostrado() { _mensaje.value = null }
}
