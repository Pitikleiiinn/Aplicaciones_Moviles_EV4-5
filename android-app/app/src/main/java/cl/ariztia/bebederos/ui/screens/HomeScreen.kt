package cl.ariztia.bebederos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.data.model.Rol
import cl.ariztia.bebederos.data.model.Usuario
import cl.ariztia.bebederos.ui.components.ItemCard
import cl.ariztia.bebederos.ui.components.PantallaBase
import cl.ariztia.bebederos.viewmodel.AlertasViewModel
import cl.ariztia.bebederos.viewmodel.UiState

/**
 * [PANTALLA] Inicio. Muestra un menú distinto según el rol (el caso define dos perfiles):
 *  - OPERARIO: consulta temperaturas, registra flushing, ve alertas e historial.
 *  - SUPERVISOR: revisa granjas/líneas, atiende alertas y hace seguimiento del historial de flushing.
 */
@Composable
fun HomeScreen(
    usuario: Usuario?,
    alertasVm: AlertasViewModel,
    onGranjas: () -> Unit,
    onFlushing: () -> Unit,
    onHistorial: () -> Unit,
    onAlertas: () -> Unit,
    onSalir: () -> Unit
) {
    // Carga alertas al entrar para mostrar el contador en la tarjeta de alertas
    LaunchedEffect(Unit) { alertasVm.cargar() }
    val estadoAlertas by alertasVm.estado.collectAsStateWithLifecycle()
    val activas = (estadoAlertas as? UiState.Exito)?.datos?.size ?: 0
    val esSupervisor = usuario?.rol == Rol.SUPERVISOR

    PantallaBase(
        titulo = if (esSupervisor) "Panel Supervisor" else "Panel Operario",
        acciones = {
            IconButton(onClick = onSalir) { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión") }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Hola, ${usuario?.nombre ?: ""}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                if (esSupervisor) "Revisa el estado de las líneas y da seguimiento a las acciones."
                else "Consulta temperaturas y registra tus acciones de flushing.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            ItemCard(
                titulo = if (esSupervisor) "Revisar granjas y líneas" else "Consultar temperaturas",
                subtitulo = "Granja › Galpón › Línea de bebederos",
                icono = Icons.Filled.Agriculture, onClick = onGranjas
            )
            ItemCard(
                titulo = "Alertas activas", subtitulo = "Temperaturas en advertencia o críticas",
                icono = Icons.Filled.Notifications, onClick = onAlertas,
                extra = { if (activas > 0) Badge { Text("$activas") } }
            )
            // Solo el operario registra flushing (el supervisor hace seguimiento)
            if (!esSupervisor) {
                ItemCard(
                    titulo = "Registrar flushing", subtitulo = "Limpieza de bebederos realizada",
                    icono = Icons.Filled.WaterDrop, onClick = onFlushing
                )
            }
            ItemCard(
                titulo = "Historial de flushing",
                subtitulo = if (esSupervisor) "Seguimiento de acciones" else "Ver, corregir o eliminar registros",
                icono = Icons.Filled.History, onClick = onHistorial
            )
        }
    }
}
