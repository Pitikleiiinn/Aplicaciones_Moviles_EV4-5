package cl.ariztia.bebederos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.data.model.EstadoLinea
import cl.ariztia.bebederos.ui.components.EstadoChip
import cl.ariztia.bebederos.ui.components.PantallaBase
import cl.ariztia.bebederos.viewmodel.RegistrarTemperaturaViewModel

/** [PANTALLA] Formulario para registrar la temperatura medida (CREATE en ms-temperaturas). */
@Composable
fun RegistrarTemperaturaScreen(vm: RegistrarTemperaturaViewModel, onVolver: () -> Unit) {
    LaunchedEffect(Unit) { vm.cargarLinea() }
    val ui by vm.ui.collectAsStateWithLifecycle()
    val linea by vm.linea.collectAsStateWithLifecycle()

    // Al guardarse con éxito se vuelve al detalle (que se recarga solo)
    LaunchedEffect(ui.exito) { if (ui.exito != null) onVolver() }

    // Vista previa local del estado mientras el usuario escribe (el oficial lo calcula el servidor)
    val previa = ui.texto.replace(',', '.').toDoubleOrNull()?.let { EstadoLinea.desdeTemperatura(it) }

    PantallaBase(titulo = "Registrar temperatura", onVolver = onVolver) { padding ->
        ConPadding(padding) {
            Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(linea?.descripcionCompleta ?: "Cargando línea...", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = ui.texto, onValueChange = vm::cambiarTexto,
                    label = { Text("Temperatura del agua (°C)") },
                    isError = ui.error != null, supportingText = { ui.error?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                if (previa != null && ui.error == null) {
                    Text("Estado estimado:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    EstadoChip(previa)
                }
                Button(onClick = vm::guardar, enabled = !ui.enviando, modifier = Modifier.fillMaxWidth()) {
                    Text(if (ui.enviando) "Guardando..." else "Guardar lectura")
                }
            }
        }
    }
}
