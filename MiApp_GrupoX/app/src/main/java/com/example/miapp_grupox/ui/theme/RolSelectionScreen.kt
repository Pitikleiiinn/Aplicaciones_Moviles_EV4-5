
package com.example.miapp_grupox.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RolSelectionScreen(
    onOperario: () -> Unit,
    onSupervisor: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        AriztiaHeader(
            titulo = "Seleccionar perfil",
            subtitulo = "Ingresa según tu función"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AriztiaSectionTitle(
                "¿Cómo deseas ingresar?",
                "Selecciona el panel que necesitas utilizar."
            )

            AriztiaWhiteCard {
                Text("Operario", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Registrar temperaturas y flushing, consultar líneas y revisar alertas.",
                    color = TextSecondary
                )
                Spacer(Modifier.height(16.dp))
                AriztiaPrimaryButton("Ingresar como operario", onOperario)
            }

            AriztiaWhiteCard {
                Text("Supervisor", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Consultar estados, alertas y registros históricos.",
                    color = TextSecondary
                )
                Spacer(Modifier.height(16.dp))
                AriztiaSecondaryButton("Ingresar como supervisor", onSupervisor)
            }
        }
    }
}