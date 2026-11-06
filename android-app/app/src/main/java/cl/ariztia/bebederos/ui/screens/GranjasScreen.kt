package cl.ariztia.bebederos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.ui.components.ContenidoUiState
import cl.ariztia.bebederos.ui.components.ItemCard
import cl.ariztia.bebederos.ui.components.PantallaBase
import cl.ariztia.bebederos.viewmodel.GranjasViewModel

/** [PANTALLA] Nivel 1: lista de granjas (datos desde ms-granjas). */
@Composable
fun GranjasScreen(vm: GranjasViewModel, onVolver: () -> Unit, onGranja: (Long) -> Unit) {
    LaunchedEffect(Unit) { vm.cargar() }
    val estado by vm.estado.collectAsStateWithLifecycle()

    PantallaBase(titulo = "Granjas", onVolver = onVolver) { padding ->
        ConPadding(padding) {
            ContenidoUiState(estado, onReintentar = vm::cargar) { granjas ->
                LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(granjas, key = { it.id }) { g ->
                        ItemCard(titulo = g.nombre, subtitulo = g.ubicacion, icono = Icons.Filled.Agriculture, onClick = { onGranja(g.id) })
                    }
                }
            }
        }
    }
}

/** Contenedor que aplica el padding del Scaffold a su contenido */
@Composable
internal fun ConPadding(padding: androidx.compose.foundation.layout.PaddingValues, content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Box(Modifier.padding(padding).fillMaxSize()) { content() }
}
