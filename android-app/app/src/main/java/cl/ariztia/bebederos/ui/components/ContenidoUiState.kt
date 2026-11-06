package cl.ariztia.bebederos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.ariztia.bebederos.viewmodel.UiState

/**
 * [COMPONENTE] Dibuja según el estado: círculo de carga, mensaje de error con botón "Reintentar",
 * o el contenido real cuando los datos llegaron. Evita repetir este patrón en cada pantalla.
 */
@Composable
fun <T> ContenidoUiState(
    estado: UiState<T>,
    onReintentar: () -> Unit,
    contenido: @Composable (T) -> Unit
) {
    when (estado) {
        is UiState.Cargando -> Column(
            Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
        ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }

        is UiState.Error -> Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Filled.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(estado.mensaje, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 12.dp))
            Button(onClick = onReintentar) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
                Text("  Reintentar")
            }
        }

        is UiState.Exito -> contenido(estado.datos)
    }
}
