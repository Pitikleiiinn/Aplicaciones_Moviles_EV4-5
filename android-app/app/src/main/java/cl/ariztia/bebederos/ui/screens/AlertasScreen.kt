package cl.ariztia.bebederos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.ui.components.ContenidoUiState
import cl.ariztia.bebederos.ui.components.EstadoChip
import cl.ariztia.bebederos.ui.components.PantallaBase
import cl.ariztia.bebederos.util.AlertNotifier
import cl.ariztia.bebederos.util.FechaUtil
import cl.ariztia.bebederos.viewmodel.AlertasViewModel
import cl.ariztia.bebederos.viewmodel.UiState

/** [PANTALLA] Alertas activas. Además lanza una notificación del sistema por cada alerta crítica nueva. */
@Composable
fun AlertasScreen(vm: AlertasViewModel, onVolver: () -> Unit) {
    val contexto = LocalContext.current
    LaunchedEffect(Unit) { vm.cargar() }
    val estado by vm.estado.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) { mensaje?.let { snackbar.showSnackbar(it); vm.mensajeMostrado() } }

    // [NOTIFICACIONES] Cada vez que llegan alertas, se avisa de las críticas pendientes
    LaunchedEffect(estado) { (estado as? UiState.Exito)?.let { AlertNotifier.notificarCriticas(contexto, it.datos) } }

    PantallaBase(titulo = "Alertas activas", onVolver = onVolver, snackbarHostState = snackbar) { padding ->
        ConPadding(padding) {
            ContenidoUiState(estado, onReintentar = vm::cargar) { alertas ->
                if (alertas.isEmpty()) {
                    Column(Modifier.fillMaxSize().padding(24.dp)) { Text("No hay alertas activas. ¡Todo en orden!") }
                } else {
                    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(alertas, key = { it.id }) { a ->
                            Card(Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        EstadoChip(a.estado)
                                        Text(FechaUtil.formatear(a.fechaHora), style = MaterialTheme.typography.bodySmall)
                                    }
                                    Text(a.lineaNombre ?: "Línea ${a.lineaId}", fontWeight = FontWeight.Bold)
                                    Text(a.mensaje)
                                    OutlinedButton(onClick = { vm.atender(a.id) }) { Text("Marcar como atendida") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
