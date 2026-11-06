package cl.ariztia.bebederos.viewmodel

/** [ESTADO DE UI] Los 3 estados posibles de cualquier pantalla que carga datos de la red. */
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}
