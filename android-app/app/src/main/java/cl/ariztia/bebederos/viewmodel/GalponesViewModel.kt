package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ariztia.bebederos.data.model.Galpon
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** [VIEWMODEL] Nivel 2: galpones de una granja (el id llega por argumento de navegación). */
class GalponesViewModel(private val repo: GranjasRepository, private val granjaId: Long) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Galpon>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Galpon>>> = _estado.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            _estado.value = when (val r = repo.galpones(granjaId)) {
                is Resultado.Exito -> UiState.Exito(r.datos)
                is Resultado.Error -> UiState.Error(r.mensaje)
            }
        }
    }
}
