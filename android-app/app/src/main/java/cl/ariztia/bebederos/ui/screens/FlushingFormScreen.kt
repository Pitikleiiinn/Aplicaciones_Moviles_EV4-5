package cl.ariztia.bebederos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.ui.components.PantallaBase
import cl.ariztia.bebederos.viewmodel.FlushingFormViewModel

/** [COMPONENTE LOCAL] Menú desplegable genérico: evita escribir a mano (como pedía la clase con "Granja"). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> Selector(
    etiqueta: String, seleccionado: String, opciones: List<T>, nombre: (T) -> String,
    error: String?, habilitado: Boolean = true, onElegir: (T) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = abierto, onExpandedChange = { if (habilitado) abierto = it }) {
        OutlinedTextField(
            value = seleccionado, onValueChange = {}, readOnly = true, enabled = habilitado,
            label = { Text(etiqueta) }, isError = error != null, supportingText = { error?.let { Text(it) } },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = abierto) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            opciones.forEach { op ->
                DropdownMenuItem(text = { Text(nombre(op)) }, onClick = { onElegir(op); abierto = false })
            }
        }
    }
}

/** [PANTALLA] Formulario "Registrar flushing" con selección en cascada, validaciones y mensaje de éxito/error. */
@Composable
fun FlushingFormScreen(vm: FlushingFormViewModel, onVolver: () -> Unit) {
    LaunchedEffect(Unit) { vm.cargarGranjas() }
    val ui by vm.ui.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(ui.mensaje) { ui.mensaje?.let { snackbar.showSnackbar(it); vm.mensajeMostrado() } }

    PantallaBase(titulo = "Registrar flushing", onVolver = onVolver, snackbarHostState = snackbar) { padding ->
        ConPadding(padding) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ui.errorCatalogo?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Selector("Granja", ui.granja?.nombre ?: "", ui.granjas, { it.nombre }, ui.errores["granja"]) { vm.seleccionarGranja(it) }
                Selector("Galpón", ui.galpon?.nombre ?: "", ui.galpones, { it.nombre }, ui.errores["galpon"], habilitado = ui.granja != null) { vm.seleccionarGalpon(it) }
                Selector("Línea de bebederos", ui.linea?.nombre ?: "", ui.lineas, { it.nombre }, ui.errores["linea"], habilitado = ui.galpon != null) { vm.seleccionarLinea(it) }

                OutlinedTextField(
                    value = ui.responsable, onValueChange = vm::cambiarResponsable, label = { Text("Responsable") },
                    isError = ui.errores["responsable"] != null, supportingText = { ui.errores["responsable"]?.let { Text(it) } },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ui.duracion, onValueChange = vm::cambiarDuracion, label = { Text("Duración (minutos)") },
                    isError = ui.errores["duracion"] != null, supportingText = { ui.errores["duracion"]?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ui.observacion, onValueChange = vm::cambiarObservacion, label = { Text("Observación") },
                    isError = ui.errores["observacion"] != null, supportingText = { ui.errores["observacion"]?.let { Text(it) } },
                    minLines = 3, modifier = Modifier.fillMaxWidth()
                )

                Button(onClick = vm::guardar, enabled = !ui.enviando, modifier = Modifier.fillMaxWidth()) {
                    Text(if (ui.enviando) "Guardando..." else "Registrar flushing")
                }
            }
        }
    }
}
