package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ariztia.bebederos.data.model.Granja
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** [VIEWMODEL] Nivel 1 de la navegación: lista de granjas. */
class GranjasViewModel(private val repo: GranjasRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Granja>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Granja>>> = _estado.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            _estado.value = when (val r = repo.granjas()) {
                is Resultado.Exito -> UiState.Exito(r.datos)
                is Resultado.Error -> UiState.Error(r.mensaje)
            }
        }
    }
}
