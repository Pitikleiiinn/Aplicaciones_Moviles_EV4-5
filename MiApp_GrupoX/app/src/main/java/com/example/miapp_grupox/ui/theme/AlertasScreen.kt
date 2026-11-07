package com.example.miapp_grupox.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miapp_grupox.model.LineBebedero
import com.example.miapp_grupox.model.RangosTemperatura

@Composable
fun AlertasScreen(
    lineas: List<LineBebedero>,
    onVolver: () -> Unit
) {
    val alertas = lineas.filter { it.estado != "Normal" }
    val criticas = alertas.count { it.estado == "Crítico" }
    val advertencias = alertas.count { it.estado == "Advertencia" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        AriztiaHeader(
            "Centro de alertas",
            "Seguimiento de las condiciones registradas"
        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AriztiaMetricCard(
                        "Críticas", criticas.toString(), "Estado crítico",
                        EstadoCritico, Modifier.weight(1f)
                    )
                    AriztiaMetricCard(
                        "Advertencias", advertencias.toString(), "Requieren revisión",
                        EstadoAdvertenciaTexto, Modifier.weight(1f)
                    )
                }
            }

            item {
                AriztiaSectionTitle(
                    "Alertas activas",
                    "Las líneas normales no aparecen en esta lista."
                )
            }

            if (alertas.isEmpty()) {
                item {
                    AriztiaWhiteCard {
                        Text("Sin alertas activas", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "Todas las líneas están en estado normal según los límites configurados.",
                            color = TextSecondary
                        )
                    }
                }
            }

            items(alertas, key = { it.id }) { linea ->
                AriztiaWhiteCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(linea.granja, style = MaterialTheme.typography.titleMedium)
                            Text("${linea.galpon} · ${linea.linea}", color = TextSecondary)
                        }
                        EstadoBadge(linea.estado)
                    }

                    Spacer(Modifier.height(10.dp))

                    Text("Rango de temperatura", color = TextSecondary)

                    // Se muestra el rango, no el número exacto
                    Text(
                        RangosTemperatura.etiquetaDe(linea.temperatura),
                        style = MaterialTheme.typography.titleLarge,
                        color = if (linea.estado == "Crítico") EstadoCritico else EstadoAdvertenciaTexto
                    )

                    Text("Fecha: ${linea.fecha}", color = TextSecondary)
                }
            }

            item {
                AriztiaSecondaryButton("Volver", onVolver)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}