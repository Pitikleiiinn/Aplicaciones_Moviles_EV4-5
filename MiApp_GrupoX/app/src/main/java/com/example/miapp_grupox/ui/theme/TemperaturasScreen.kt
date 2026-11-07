package com.example.miapp_grupox.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.miapp_grupox.model.LineBebedero
import com.example.miapp_grupox.model.OpcionesBebedero
import com.example.miapp_grupox.model.RangosTemperatura
import com.example.miapp_grupox.model.ValidadorTemperatura
import com.example.miapp_grupox.model.vibrarConfirmacion
import com.example.miapp_grupox.remote.ClimaActual
import kotlinx.coroutines.launch

@Composable
fun TemperaturasScreen(
    lineas: List<LineBebedero>,
    onGuardar: (String, String, String, Double) -> Unit,
    onVolver: () -> Unit,
    // Clima exterior de la granja elegida (API externa Open-Meteo); null = no disponible
    clima: ClimaActual? = null,
    onGranjaCambiada: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // Estado y corrutina para mostrar mensajes al pie de la pantalla (Snackbar)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var granja by remember { mutableStateOf(OpcionesBebedero.granjas[0]) }
    var galpon by remember { mutableStateOf(OpcionesBebedero.galpones[0]) }
    var linea by remember { mutableStateOf(OpcionesBebedero.lineas[0]) }
    var rango by remember { mutableStateOf("") }

    // Cada vez que cambia la granja se pide el clima exterior a la API externa
    LaunchedEffect(granja) { onGranjaCambiada(granja) }

    // Errores por campo que entrega el validador (lógica fuera de la pantalla)
    var errores by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = AppBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AriztiaHeader(
                "Control de temperaturas",
                "Mediciones por granja, galpón y línea"
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    AriztiaSectionTitle("Registrar medición")
                }

                item {
                    AriztiaWhiteCard {
                        AriztiaDropdown(
                            label = "Granja",
                            opciones = OpcionesBebedero.granjas,
                            seleccion = granja,
                            onSeleccion = { granja = it }
                        )

                        Spacer(Modifier.height(8.dp))

                        AriztiaDropdown(
                            label = "Galpón",
                            opciones = OpcionesBebedero.galpones,
                            seleccion = galpon,
                            onSeleccion = { galpon = it }
                        )

                        Spacer(Modifier.height(8.dp))

                        AriztiaDropdown(
                            label = "Línea",
                            opciones = OpcionesBebedero.lineas,
                            seleccion = linea,
                            onSeleccion = { linea = it }
                        )

                        Spacer(Modifier.height(8.dp))

                        AriztiaDropdown(
                            label = "Rango de temperatura",
                            opciones = RangosTemperatura.etiquetas,
                            seleccion = rango,
                            onSeleccion = {
                                rango = it
                                errores = errores - "rango"
                            },
                            isError = errores.containsKey("rango")
                        )

                        errores["rango"]?.let { mensaje ->
                            Spacer(Modifier.height(4.dp))
                            Text(mensaje, color = AriztiaRed)
                        }

                        Spacer(Modifier.height(12.dp))

                        AriztiaLightButton(
                            texto = "Guardar temperatura",
                            onClick = {
                                // La validación se hace en ValidadorTemperatura, no aquí
                                val resultado = ValidadorTemperatura.validar(rango)
                                errores = resultado.errores
                                val valor = RangosTemperatura.valorDe(rango)

                                if (!resultado.valido || valor == null) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Revisa los campos marcados"
                                        )
                                    }
                                } else {
                                    onGuardar(granja, galpon, linea, valor)

                                    // Vibración como confirmación táctil al guardar
                                    vibrarConfirmacion(context)

                                    rango = ""
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Temperatura registrada correctamente"
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                // Clima exterior de la granja seleccionada (consumo de API externa con Retrofit)
                item {
                    AriztiaWhiteCard {
                        Text(
                            "Clima exterior · $granja",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            if (clima != null) {
                                "${clima.temperatura} °C · Humedad ${clima.humedad}%"
                            } else {
                                "Clima no disponible (sin conexión a la API externa)"
                            },
                            color = TextSecondary
                        )
                    }
                }

                item {
                    AriztiaSectionTitle(
                        "Líneas registradas",
                        "${lineas.size} líneas en la base de datos"
                    )
                }

                items(lineas, key = { it.id }) { item ->
                    TarjetaLinea(item)
                }

                item {
                    AriztiaSecondaryButton("Volver", onVolver)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun TarjetaLinea(linea: LineBebedero) {
    // Controla si el detalle de la tarjeta está visible
    var mostrarDetalle by remember { mutableStateOf(false) }

    val colorTexto = when (linea.estado) {
        "Crítico" -> EstadoCritico
        "Advertencia" -> EstadoAdvertenciaTexto
        else -> EstadoNormal
    }

    // Animación: el color de fondo cambia suavemente según el estado
    val colorCard by animateColorAsState(
        targetValue = when (linea.estado) {
            "Crítico" -> Color(0xFFFFCDD2)      // rojo claro
            "Advertencia" -> Color(0xFFFFF3CD)  // amarillo
            else -> Color(0xFFE8F5E9)           // verde claro
        },
        animationSpec = tween(durationMillis = 600),
        label = "colorCard"
    )

    // Animación: la sombra de la tarjeta sube al abrir el detalle
    val elevacion by animateDpAsState(
        targetValue = if (mostrarDetalle) 8.dp else 2.dp,
        label = "elevacionCard"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(), // la tarjeta crece/encoge con animación
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorCard),
        elevation = CardDefaults.cardElevation(defaultElevation = elevacion)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        linea.granja,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "${linea.galpon} · ${linea.linea}",
                        color = TextSecondary
                    )
                }
                EstadoBadge(linea.estado)
            }

            Spacer(Modifier.height(12.dp))

            Text("Rango de temperatura", color = TextSecondary)

            Text(
                RangosTemperatura.etiquetaDe(linea.temperatura),
                style = MaterialTheme.typography.headlineSmall,
                color = colorTexto
            )

            Spacer(Modifier.height(4.dp))

            TextButton(onClick = { mostrarDetalle = !mostrarDetalle }) {
                Text(
                    if (mostrarDetalle) "Ocultar detalle" else "Ver detalle",
                    color = AriztiaButtonRed
                )
            }

            // Animación: el detalle aparece y desaparece con efecto
            AnimatedVisibility(visible = mostrarDetalle) {
                Column {
                    Text("Última medición: ${linea.fecha}", color = TextSecondary)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Criterio: Normal menos de 27 °C · Advertencia de 27 a 29,9 °C · Crítico desde 30 °C",
                        color = TextSecondary
                    )
                }
            }
        }
    }
}