
package com.example.miapp_grupox.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OperarioScreen(
    onTemperaturas: () -> Unit,
    onFlushing: () -> Unit,
    onAlertas: () -> Unit,
    onInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        AriztiaHeader(
            titulo = "Panel del operario",
            subtitulo = "Control diario de las instalaciones"
        )

        LazyColumn(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                AriztiaSectionTitle(
                    "Acciones operativas",
                    "Selecciona la actividad que deseas realizar."
                )
            }

            item {
                AccionOperario(
                    titulo = "Temperaturas",
                    descripcion = "Registrar y consultar mediciones",
                    onClick = onTemperaturas
                )
            }

            item {
                AccionOperario(
                    titulo = "Registro de flushing",
                    descripcion = "Registrar y consultar limpiezas",
                    onClick = onFlushing
                )
            }

            item {
                AccionOperario(
                    titulo = "Alertas",
                    descripcion = "Revisar advertencias y estados críticos",
                    onClick = onAlertas
                )
            }

            item {
                AriztiaSecondaryButton(
                    texto = "Volver al inicio",
                    onClick = onInicio
                )
            }
        }
    }
}

@Composable
private fun AccionOperario(
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(5.dp))

            Text(
                descripcion,
                color = TextSecondary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Abrir →",
                color = AriztiaRed
            )
        }
    }
}