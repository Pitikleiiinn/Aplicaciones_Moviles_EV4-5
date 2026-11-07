package com.example.miapp_grupox.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(

    primary = AriztiaRed,

    onPrimary = CardBackground,

    secondary = EstadoNormal,

    background = AppBackground,

    onBackground = TextPrimary,

    surface = CardBackground,

    onSurface = TextPrimary,

    error = EstadoCritico
)


@Composable
fun MiAppGrupoXTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}