
package com.example.miapp_grupox.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miapp_grupox.model.FlusingEntity
import com.example.miapp_grupox.model.LineBebedero

@Composable
fun SupervisorScreen(
    lineas: List<LineBebedero>,
    registros: List<FlusingEntity>,
    onTemperaturas: () -> Unit,
    onAlertas: () -> Unit,
    onFlushing: () -> Unit,
    onInicio: () -> Unit
) {
    val criticas = lineas.count { it.estado == "Crítico" }
    val advertencias = lineas.count { it.estado == "Advertencia" }
    val normales = lineas.count { it.estado == "Normal" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        AriztiaHeader(
            titulo = "Panel del supervisor",
            subtitulo = "Estado general de las instalaciones"
        )

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AriztiaSectionTitle(
                    "Resumen operativo",
                    "Estado de las líneas registradas"
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AriztiaMetricCard(
                        titulo = "Críticas",
                        valor = criticas.toString(),
                        descripcion = "Revisar",
                        color = EstadoCritico,
                        modifier = Modifier.weight(1f)
                    )

                    AriztiaMetricCard(
                        titulo = "Advertencias",
                        valor = advertencias.toString(),
                        descripcion = "Seguimiento",
                        color = EstadoAdvertenciaTexto,
                        modifier = Modifier.weight(1f)
                    )

                    AriztiaMetricCard(
                        titulo = "Normales",
                        valor = normales.toString(),
                        descripcion = "Estables",
                        color = EstadoNormal,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                AriztiaWhiteCard {
                    Text(
                        "Temperaturas",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "${lineas.size} líneas registradas",
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(12.dp))
                    AriztiaPrimaryButton(
                        "Consultar temperaturas",
                        onTemperaturas
                    )
                }
            }

            item {
                AriztiaWhiteCard {
                    Text(
                        "Centro de alertas",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "${criticas + advertencias} líneas requieren revisión.",
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(12.dp))
                    AriztiaPrimaryButton(
                        "Ver alertas",
                        onAlertas
                    )
                }
            }

            item {
                AriztiaWhiteCard {
                    Text(
                        "Historial de flushing",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "${registros.size} registros guardados",
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(12.dp))
                    AriztiaSecondaryButton(
                        "Consultar flushing",
                        onFlushing
                    )
                }
            }

            item {
                AriztiaSecondaryButton(
                    "Volver al inicio",
                    onInicio
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}