package com.example.miapp_grupox.ui.theme

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.miapp_grupox.model.FlusingEntity
import com.example.miapp_grupox.model.OpcionesBebedero
import com.example.miapp_grupox.model.ValidadorFlushing
import com.example.miapp_grupox.model.vibrarConfirmacion
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FlusingScreen(
    registros: List<FlusingEntity>,
    onGuardar: (FlusingEntity) -> Unit,
    onEliminar: (Int) -> Unit,
    onActualizarObservacion: (FlusingEntity, String) -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    // Estado y corrutina para mostrar mensajes al pie de la pantalla (Snackbar)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var granja by remember { mutableStateOf(OpcionesBebedero.granjas[0]) }
    var galpon by remember { mutableStateOf(OpcionesBebedero.galpones[0]) }
    var linea by remember { mutableStateOf(OpcionesBebedero.lineas[0]) }
    var motivo by remember { mutableStateOf("") }
    var responsable by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }

    // Foto de evidencia tomada con la cámara (se guarda como Bitmap)
    var foto by remember { mutableStateOf<Bitmap?>(null) }

    // Errores por campo que entrega el validador (lógica fuera de la pantalla)
    var errores by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    // Estado de los diálogos de eliminar y editar del historial
    var registroAEliminar by remember { mutableStateOf<FlusingEntity?>(null) }
    var registroEditando by remember { mutableStateOf<FlusingEntity?>(null) }
    var textoEdicion by remember { mutableStateOf("") }
    var errorEdicion by remember { mutableStateOf<String?>(null) }

    // Recurso nativo: abre la aplicación de cámara y devuelve la foto
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            foto = bitmap
            errores = errores - "foto"
        }
    }

    // Pide el permiso de cámara al usuario; si lo acepta, abre la cámara
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            camaraLauncher.launch(null)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "Sin permiso de cámara no se puede adjuntar evidencia"
                )
            }
        }
    }

    // Revisa si el permiso ya fue entregado antes de abrir la cámara
    fun tomarFoto() {
        val tienePermiso = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermiso) {
            camaraLauncher.launch(null)
        } else {
            permisoLauncher.launch(Manifest.permission.CAMERA)
        }
    }

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
                "Registro de flushing",
                "Control de limpieza de líneas de bebederos"
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    AriztiaSectionTitle(
                        "Nuevo registro",
                        "Completa los datos de la actividad realizada."
                    )
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
                            label = "Línea de bebederos",
                            opciones = OpcionesBebedero.lineas,
                            seleccion = linea,
                            onSeleccion = { linea = it }
                        )

                        Spacer(Modifier.height(8.dp))

                        // Responsable: ícono de persona y error por campo
                        AriztiaTextField(
                            value = responsable,
                            onValueChange = {
                                responsable = it
                                errores = errores - "responsable"
                            },
                            label = "Responsable",
                            iconoInicial = Icons.Default.Person,
                            error = errores["responsable"]
                        )

                        Spacer(Modifier.height(8.dp))

                        AriztiaDropdown(
                            label = "Motivo del flushing",
                            opciones = OpcionesBebedero.motivosFlushing,
                            seleccion = motivo,
                            onSeleccion = {
                                motivo = it
                                errores = errores - "motivo"
                            },
                            isError = errores.containsKey("motivo")
                        )

                        errores["motivo"]?.let { mensaje ->
                            Spacer(Modifier.height(4.dp))
                            Text(mensaje, color = AriztiaRed)
                        }

                        Spacer(Modifier.height(8.dp))

                        // Observaciones: ícono de lápiz y error por campo
                        AriztiaTextField(
                            value = observaciones,
                            onValueChange = {
                                observaciones = it
                                errores = errores - "observaciones"
                            },
                            label = "Observaciones",
                            iconoInicial = Icons.Default.Edit,
                            error = errores["observaciones"],
                            singleLine = false,
                            minLines = 3
                        )

                        Spacer(Modifier.height(12.dp))

                        // Botón con ícono de cámara (evidencia del flushing)
                        AriztiaCameraButton(onClick = { tomarFoto() })

                        // La foto aparece con animación debajo del botón
                        AnimatedVisibility(visible = foto != null) {
                            foto?.let { bitmap ->
                                Column {
                                    Spacer(Modifier.height(8.dp))
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Foto de evidencia del flushing",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }
                            }
                        }

                        errores["foto"]?.let { mensaje ->
                            Spacer(Modifier.height(4.dp))
                            Text(mensaje, color = AriztiaRed)
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Fecha y hora: ${fechaActual()}",
                            color = TextSecondary
                        )

                        Spacer(Modifier.height(12.dp))

                        AriztiaPrimaryButton(
                            texto = "Guardar registro",
                            onClick = {
                                // La validación se hace en ValidadorFlushing, no aquí
                                val resultado = ValidadorFlushing.validar(
                                    responsable = responsable,
                                    motivo = motivo,
                                    observaciones = observaciones,
                                    tieneFoto = foto != null
                                )
                                errores = resultado.errores

                                if (!resultado.valido) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Revisa los campos marcados"
                                        )
                                    }
                                } else {
                                    onGuardar(
                                        FlusingEntity(
                                            granja = granja,
                                            galpon = galpon,
                                            linea = linea,
                                            responsable = responsable.trim(),
                                            motivo = motivo,
                                            observaciones = observaciones.trim(),
                                            fecha = fechaActual()
                                        )
                                    )

                                    // Vibración como confirmación táctil al guardar
                                    vibrarConfirmacion(context)

                                    responsable = ""
                                    motivo = ""
                                    observaciones = ""
                                    foto = null
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Flushing registrado correctamente"
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                item {
                    AriztiaSectionTitle(
                        "Historial de flushing",
                        "${registros.size} registros guardados"
                    )
                }

                if (registros.isEmpty()) {
                    item {
                        AriztiaWhiteCard {
                            Text("Sin registros", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Los registros que guardes aparecerán aquí.",
                                color = TextSecondary
                            )
                        }
                    }
                }

                items(registros, key = { it.id }) { registro ->
                    AriztiaWhiteCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "${registro.granja} · ${registro.galpon}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(registro.linea, color = AriztiaRed)
                            }

                            EstadoFlushingAnimado(registro.id)
                        }

                        HorizontalDivider(Modifier.padding(vertical = 10.dp))

                        TextoRegistro("Responsable", registro.responsable)
                        TextoRegistro("Motivo", registro.motivo)
                        TextoRegistro("Fecha", registro.fecha)

                        if (registro.observaciones.isNotBlank()) {
                            TextoRegistro("Observaciones", registro.observaciones)
                        }

                        Spacer(Modifier.height(8.dp))

                        // Modificar y eliminar un registro del historial
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    registroEditando = registro
                                    textoEdicion = registro.observaciones
                                    errorEdicion = null
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = AriztiaButtonRed
                                )
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Editar")
                            }

                            OutlinedButton(
                                onClick = { registroAEliminar = registro },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = AriztiaButtonRed
                                )
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Eliminar")
                            }
                        }
                    }
                }

                item {
                    AriztiaSecondaryButton("Volver", onVolver)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }

    // Diálogo de confirmación antes de eliminar un registro
    registroAEliminar?.let { registro ->
        AlertDialog(
            onDismissRequest = { registroAEliminar = null },
            title = { Text("Eliminar registro") },
            text = {
                Text(
                    "¿Seguro que quieres eliminar el flushing de ${registro.linea} " +
                            "(${registro.fecha})? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onEliminar(registro.id)
                    registroAEliminar = null
                    scope.launch {
                        snackbarHostState.showSnackbar("Registro eliminado")
                    }
                }) {
                    Text("Eliminar", color = AriztiaButtonRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { registroAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo para corregir la observación de un registro
    registroEditando?.let { registro ->
        AlertDialog(
            onDismissRequest = { registroEditando = null },
            title = { Text("Editar observación") },
            text = {
                AriztiaTextField(
                    value = textoEdicion,
                    onValueChange = {
                        textoEdicion = it
                        errorEdicion = null
                    },
                    label = "Observaciones",
                    iconoInicial = Icons.Default.Edit,
                    error = errorEdicion,
                    singleLine = false,
                    minLines = 3
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    // La validación está en ValidadorFlushing, no en la pantalla
                    val error = ValidadorFlushing.validarObservacion(textoEdicion)
                    if (error != null) {
                        errorEdicion = error
                    } else {
                        onActualizarObservacion(registro, textoEdicion)
                        registroEditando = null
                        scope.launch {
                            snackbarHostState.showSnackbar("Observación actualizada")
                        }
                    }
                }) {
                    Text("Guardar", color = AriztiaButtonRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { registroEditando = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TextoRegistro(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text("$etiqueta: ", color = TextPrimary)
        Text(valor, color = TextSecondary)
    }
}

private fun fechaActual(): String {
    return SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    ).format(Date())
}