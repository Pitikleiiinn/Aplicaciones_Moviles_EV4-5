package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * [FÁBRICA] Permite crear ViewModels que reciben parámetros en el constructor
 * (repositorios, ids de navegación). Uso: viewModel(key = "x", factory = fabrica { MiViewModel(...) })
 */
fun <T : ViewModel> fabrica(crear: () -> T): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <V : ViewModel> create(modelClass: Class<V>): V = crear() as V
    }
