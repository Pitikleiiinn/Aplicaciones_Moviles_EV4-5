package com.example.miapp_grupox.ui.theme


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Simula el cambio de estado de un flushing: "En proceso" -> "Completado"
@Composable
fun EstadoFlushingAnimado(clave: Any) {
    var estado by remember(clave) { mutableStateOf("En proceso") }

    // Se ejecuta al mostrarse la tarjeta: espera 3 segundos y cambia el estado
    LaunchedEffect(clave) {
        delay(3000)
        estado = "Completado"
    }

    val completado = estado == "Completado"

    // El color cambia con animación
    val fondo by animateColorAsState(
        targetValue = if (completado) EstadoNormalBackground else EstadoAdvertenciaBackground,
        animationSpec = tween(durationMillis = 800),
        label = "fondoEstadoFlushing"
    )
    val textoColor by animateColorAsState(
        targetValue = if (completado) EstadoNormal else EstadoAdvertenciaTexto,
        animationSpec = tween(durationMillis = 800),
        label = "textoEstadoFlushing"
    )

    Surface(
        color = fondo,
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = estado.uppercase(),
            color = textoColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}