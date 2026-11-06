package cl.ariztia.bebederos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** [TEMA] Esquema de colores Material 3 basado en el rojo de Ariztia. */
private val EsquemaAriztia = lightColorScheme(
    primary = RojoAriztia,
    onPrimary = Color.White,
    primaryContainer = RojoClaro,
    onPrimaryContainer = RojoOscuro,
    secondary = RojoOscuro,
    onSecondary = Color.White,
    background = FondoCalido,
    onBackground = GrisTexto,
    surface = Color.White,
    onSurface = GrisTexto,
    surfaceVariant = Color(0xFFF7E4E3),
    onSurfaceVariant = GrisSuave,
    error = EstadoCritico
)

@Composable
fun AriztiaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaAriztia, content = content)
}
