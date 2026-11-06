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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.data.model.Flushing
import cl.ariztia.bebederos.ui.components.ContenidoUiState
import cl.ariztia.bebederos.ui.components.PantallaBase
import cl.ariztia.bebederos.util.FechaUtil
import cl.ariztia.bebederos.viewmodel.HistorialFlushingViewModel

/** [PANTALLA] Historial de flushing (READ) con corrección (UPDATE) y eliminación (DELETE). */
@Composable
fun HistorialFlushingScreen(vm: HistorialFlushingViewModel, onVolver: () -> Unit) {
    LaunchedEffect(Unit) { vm.cargar() }
    val estado by vm.estado.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) { mensaje?.let { snackbar.showSnackbar(it); vm.mensajeMostrado() } }

    // Flushing que se está editando (null = diálogo cerrado)
    var enEdicion by remember { mutableStateOf<Flushing?>(null) }

    PantallaBase(titulo = "Historial de flushing", onVolver = onVolver, snackbarHostState = snackbar) { padding ->
        ConPadding(padding) {
            ContenidoUiState(estado, onReintentar = vm::cargar) { lista ->
                if (lista.isEmpty()) {
                    Column(Modifier.fillMaxSize().padding(24.dp)) { Text("Aún no hay flushing registrados") }
                } else {
                    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(lista, key = { it.id }) { f ->
                            Card(Modifier.fillMaxWidth()) {
                                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text("${f.granjaNombre} › ${f.galponNombre} › ${f.lineaNombre}", fontWeight = FontWeight.Bold)
                                        Text("${f.duracionMinutos} min · ${f.responsable}", style = MaterialTheme.typography.bodyMedium)
                                        Text(f.observacion, style = MaterialTheme.typography.bodyMedium)
                                        Text(FechaUtil.formatear(f.fechaRegistro), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    IconButton(onClick = { enEdicion = f }) { Icon(Icons.Filled.Edit, "Editar", tint = MaterialTheme.colorScheme.primary) }
                                    IconButton(onClick = { vm.eliminar(f.id) }) { Icon(Icons.Filled.Delete, "Eliminar", tint = MaterialTheme.colorScheme.primary) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ----- [DIÁLOGO DE EDICIÓN] -----
    enEdicion?.let { original ->
        var duracion by remember(original.id) { mutableStateOf(original.duracionMinutos.toString()) }
        var observacion by remember(original.id) { mutableStateOf(original.observacion) }
        AlertDialog(
            onDismissRequest = { enEdicion = null },
            title = { Text("Corregir flushing") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(duracion, { duracion = it.filter(Char::isDigit) }, label = { Text("Duración (min)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(observacion, { observacion = it }, label = { Text("Observación") }, minLines = 3)
                }
            },
            confirmButton = {
                TextButton(onClick = { vm.corregir(original, duracion.toIntOrNull() ?: 0, observacion); enEdicion = null }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { enEdicion = null }) { Text("Cancelar") } }
        )
    }
}
