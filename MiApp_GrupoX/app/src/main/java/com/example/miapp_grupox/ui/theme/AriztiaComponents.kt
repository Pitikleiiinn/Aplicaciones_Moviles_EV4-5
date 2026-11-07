package com.example.miapp_grupox.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================
// HEADER ARIZTIA
// ============================================================

@Composable
fun AriztiaHeader(
    titulo: String,
    subtitulo: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AriztiaRed)
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            )
    ) {

        Text(
            text = "ARIZTÍA",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = titulo,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = subtitulo,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 13.sp
        )
    }
}


// ============================================================
// TITULO DE SECCION
// ============================================================

@Composable
fun AriztiaSectionTitle(
    titulo: String,
    descripcion: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        if (descripcion.isNotBlank()) {

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}


// ============================================================
// ESTADO
// ============================================================

@Composable
fun EstadoBadge(
    estado: String
) {

    val estadoNormalizado = estado
        .trim()
        .lowercase()

    val fondo: Color
    val texto: Color

    when (estadoNormalizado) {

        "critico",
        "crítico" -> {
            fondo = EstadoCriticoBackground
            texto = EstadoCritico
        }

        "advertencia" -> {
            fondo = EstadoAdvertenciaBackground
            texto = EstadoAdvertenciaTexto
        }

        else -> {
            fondo = EstadoNormalBackground
            texto = EstadoNormal
        }
    }

    Surface(
        color = fondo,
        shape = RoundedCornerShape(50.dp)
    ) {

        Text(
            text = estado.uppercase(),
            color = texto,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            )
        )
    }
}


// ============================================================
// TARJETA DE METRICA
// ============================================================

@Composable
fun AriztiaMetricCard(
    titulo: String,
    valor: String,
    descripcion: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(4.dp)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(50.dp)
                    )
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = valor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                text = titulo,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = descripcion,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }
    }
}


// ============================================================
// BOTON PRINCIPAL
// ============================================================

@Composable
fun AriztiaPrimaryButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AriztiaButtonRed,
            contentColor = Color.White
        )
    ) {

        Text(
            text = texto,
            fontWeight = FontWeight.Bold
        )
    }
}


// ============================================================
// BOTON SECUNDARIO
// ============================================================

@Composable
fun AriztiaSecondaryButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = AriztiaButtonRed
        )
    ) {

        Text(
            text = texto,
            fontWeight = FontWeight.Bold
        )
    }
}


// ============================================================
// TARJETA BLANCA
// ============================================================

@Composable
fun AriztiaWhiteCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            content()
        }
    }
}


// ============================================================
// TARJETA DE ACCION
// ============================================================

@Composable
fun AriztiaActionCard(
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = descripcion,
                color = TextSecondary
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            AriztiaPrimaryButton(
                texto = "Abrir",
                onClick = onClick
            )
        }
    }
}