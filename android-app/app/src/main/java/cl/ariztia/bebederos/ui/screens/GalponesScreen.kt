package cl.ariztia.bebederos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.ui.components.ContenidoUiState
import cl.ariztia.bebederos.ui.components.ItemCard
import cl.ariztia.bebederos.ui.components.PantallaBase
import cl.ariztia.bebederos.viewmodel.GalponesViewModel

/** [PANTALLA] Nivel 2: galpones de la granja seleccionada. */
@Composable
fun GalponesScreen(vm: GalponesViewModel, onVolver: () -> Unit, onGalpon: (Long) -> Unit) {
    LaunchedEffect(Unit) { vm.cargar() }
    val estado by vm.estado.collectAsStateWithLifecycle()

    PantallaBase(titulo = "Galpones", onVolver = onVolver) { padding ->
        ConPadding(padding) {
            ContenidoUiState(estado, onReintentar = vm::cargar) { galpones ->
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(galpones, key = { it.id }) { g ->
                        ItemCard(titulo = g.nombre, icono = Icons.Filled.Home, onClick = { onGalpon(g.id) })
                    }
                }
            }
        }
    }
}
