package com.example.miapp_grupox.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen(
    onContinuar: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // =====================================================
        // ENCABEZADO
        // =====================================================

        AriztiaHeader(
            titulo = "Control sanitario",
            subtitulo = "Monitoreo de bebederos y flushing"
        )


        // =====================================================
        // CONTENIDO PRINCIPAL
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 20.dp
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // =================================================
            // IDENTIDAD ARIZTÍA
            // =================================================

            Surface(
                modifier = Modifier
                    .width(190.dp)
                    .height(130.dp),
                shape = RoundedCornerShape(24.dp),
                color = AriztiaLightRed
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "ARIZTÍA",
                        color = AriztiaRed,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .width(65.dp)
                            .height(4.dp)
                            .background(
                                color = AriztiaRed,
                                shape = RoundedCornerShape(50.dp)
                            )
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // =================================================
            // BIENVENIDA
            // =================================================

            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            Text(
                text = "Sistema de monitoreo y registro operativo",
                color = TextSecondary,
                fontSize = 14.sp
            )


            Spacer(
                modifier = Modifier.height(30.dp)
            )


            // =================================================
            // BOTÓN DE INGRESO
            // =================================================

            AriztiaPrimaryButton(
                texto = "Ingresar a la aplicación",
                onClick = onContinuar,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // =================================================
            // INFORMACIÓN DEL PROTOTIPO
            // =================================================

            Text(
                text = "Control sanitario · Ariztía",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "MiApp_GrupoX · Prototipo académico",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}