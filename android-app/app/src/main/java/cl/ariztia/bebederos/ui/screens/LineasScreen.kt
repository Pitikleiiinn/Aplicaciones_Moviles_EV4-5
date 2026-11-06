package cl.ariztia.bebederos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.ui.components.ContenidoUiState
import cl.ariztia.bebederos.ui.components.EstadoChip
import cl.ariztia.bebederos.ui.components.ItemCard
import cl.ariztia.bebederos.ui.components.PantallaBase
import cl.ariztia.bebederos.ui.components.color
import cl.ariztia.bebederos.viewmodel.LineasViewModel

/** [PANTALLA] Nivel 3: líneas del galpón con su última temperatura y estado (normal / advertencia / crítico). */
@Composable
fun LineasScreen(vm: LineasViewModel, onVolver: () -> Unit, onLinea: (Long) -> Unit) {
    LaunchedEffect(Unit) { vm.cargar() }
    val estado by vm.estado.collectAsStateWithLifecycle()

    PantallaBase(titulo = "Líneas de bebederos", onVolver = onVolver) { padding ->
        ConPadding(padding) {
            ContenidoUiState(estado, onReintentar = vm::cargar) { lineas ->
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(lineas, key = { it.linea.id }) { item ->
                        ItemCard(
                            titulo = item.linea.nombre,
                            subtitulo = if (item.lectura == null) "Sin lecturas aún" else "Última medición",
                            icono = Icons.Filled.Thermostat,
                            onClick = { onLinea(item.linea.id) },
                            extra = {
                                item.lectura?.let { l ->
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("${l.temperatura} °C", fontWeight = FontWeight.Bold, color = l.estado.color())
                                        EstadoChip(l.estado)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
