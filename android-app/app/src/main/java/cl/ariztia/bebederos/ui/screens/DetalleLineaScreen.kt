package cl.ariztia.bebederos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.data.model.Rol
import cl.ariztia.bebederos.ui.components.ContenidoUiState
import cl.ariztia.bebederos.ui.components.EstadoChip
import cl.ariztia.bebederos.ui.components.PantallaBase
import cl.ariztia.bebederos.ui.components.color
import cl.ariztia.bebederos.util.FechaUtil
import cl.ariztia.bebederos.viewmodel.DetalleLineaViewModel

/**
 * [PANTALLA] Detalle de una línea: temperatura actual y estado, clima exterior (API EXTERNA),
 * alertas de la línea e historial de lecturas (con opción de eliminar).
 */
@Composable
fun DetalleLineaScreen(
    vm: DetalleLineaViewModel,
    rol: Rol?,
    onVolver: () -> Unit,
    onRegistrarTemperatura: () -> Unit,
    onRegistrarFlushing: () -> Unit
) {
    LaunchedEffect(Unit) { vm.cargar() }   // se recarga cada vez que se vuelve a esta pantalla
    val estado by vm.estado.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) { mensaje?.let { snackbar.showSnackbar(it); vm.mensajeMostrado() } }

    PantallaBase(titulo = "Detalle de línea", onVolver = onVolver, snackbarHostState = snackbar) { padding ->
        ConPadding(padding) {
            ContenidoUiState(estado, onReintentar = vm::cargar) { d ->
                Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(d.linea.descripcionCompleta, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    // ----- [TEMPERATURA ACTUAL] -----
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            val u = d.ultima
                            if (u == null) {
                                Text("Sin lecturas registradas")
                            } else {
                                Text("${u.temperatura} °C", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = u.estado.color())
                                EstadoChip(u.estado)
                                Text("Medido: ${FechaUtil.formatear(u.fechaHora)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    // ----- [CLIMA EXTERIOR - API EXTERNA] -----
                    Card(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Filled.Cloud, null, tint = MaterialTheme.colorScheme.primary)
                            if (d.clima != null) {
                                Column {
                                    Text("Clima exterior de la granja", fontWeight = FontWeight.Bold)
                                    Text("${d.clima.temperatura} °C · Humedad ${d.clima.humedad}%")
                                }
                            } else {
                                Text("Clima exterior no disponible (sin conexión a la API externa)")
                            }
                        }
                    }

                    // ----- [ACCIONES] -----
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = onRegistrarTemperatura, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Filled.Add, null); Text(" Temperatura")
                        }
                        if (rol == Rol.OPERARIO) {
                            OutlinedButton(onClick = onRegistrarFlushing, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Filled.WaterDrop, null); Text(" Flushing")
                            }
                        }
                    }

                    // ----- [ALERTAS DE LA LÍNEA] -----
                    val activas = d.alertas.filter { !it.atendida }
                    Text("Alertas activas (${activas.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    if (activas.isEmpty()) Text("Sin alertas activas", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    activas.forEach { a ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            EstadoChip(a.estado)
                            Text(a.mensaje, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    // ----- [HISTORIAL DE LECTURAS] -----
                    Text("Historial", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    d.historial.forEach { l ->
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("${l.temperatura} °C", fontWeight = FontWeight.Bold, color = l.estado.color())
                                Text(FechaUtil.formatear(l.fechaHora), style = MaterialTheme.typography.bodySmall)
                            }
                            EstadoChip(l.estado)
                            IconButton(onClick = { vm.eliminarLectura(l.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar lectura", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
