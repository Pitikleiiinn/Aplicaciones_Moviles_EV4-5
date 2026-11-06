package cl.ariztia.bebederos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.ariztia.bebederos.data.model.EstadoLinea
import cl.ariztia.bebederos.ui.theme.EstadoAdvertencia
import cl.ariztia.bebederos.ui.theme.EstadoCritico
import cl.ariztia.bebederos.ui.theme.EstadoNormal

/** Color asociado a cada estado. */
fun EstadoLinea.color(): Color = when (this) {
    EstadoLinea.NORMAL -> EstadoNormal
    EstadoLinea.ADVERTENCIA -> EstadoAdvertencia
    EstadoLinea.CRITICO -> EstadoCritico
}

fun EstadoLinea.etiqueta(): String = when (this) {
    EstadoLinea.NORMAL -> "Normal"
    EstadoLinea.ADVERTENCIA -> "Advertencia"
    EstadoLinea.CRITICO -> "Crítico"
}

private fun EstadoLinea.icono(): ImageVector =
    if (this == EstadoLinea.NORMAL) Icons.Filled.CheckCircle else Icons.Filled.Warning

/** [COMPONENTE] Etiqueta de estado: color + ícono + texto (no depende solo del color). */
@Composable
fun EstadoChip(estado: EstadoLinea, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(estado.color())
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(estado.icono(), contentDescription = null, tint = Color.White, modifier = Modifier.padding(end = 4.dp))
        Text(estado.etiqueta(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
