package com.example.miapp_grupox

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.miapp_grupox.ui.theme.AlertasScreen
import com.example.miapp_grupox.ui.theme.FlusingScreen
import com.example.miapp_grupox.ui.theme.HomeScreen
import com.example.miapp_grupox.ui.theme.LoginScreen
import com.example.miapp_grupox.ui.theme.MiAppGrupoXTheme
import com.example.miapp_grupox.ui.theme.OperarioScreen
import com.example.miapp_grupox.ui.theme.RolSelectionScreen
import com.example.miapp_grupox.ui.theme.SupervisorScreen
import com.example.miapp_grupox.ui.theme.TemperaturasScreen
import com.example.miapp_grupox.viewmodel.BebederoViewModel
import com.example.miapp_grupox.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val bebederoViewModel: BebederoViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiAppGrupoXTheme {
                val lineas by bebederoViewModel.lineas
                    .collectAsStateWithLifecycle()

                val registros by mainViewModel.registros
                    .collectAsStateWithLifecycle()

                // Clima de la API externa y mensajes de red (errores de conexión, etc.)
                val clima by bebederoViewModel.clima.collectAsStateWithLifecycle()
                val mensajeLineas by bebederoViewModel.mensaje.collectAsStateWithLifecycle()
                val mensajeFlushing by mainViewModel.mensaje.collectAsStateWithLifecycle()

                LaunchedEffect(mensajeLineas) {
                    mensajeLineas?.let {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                        bebederoViewModel.mensajeMostrado()
                    }
                }
                LaunchedEffect(mensajeFlushing) {
                    mensajeFlushing?.let {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                        mainViewModel.mensajeMostrado()
                    }
                }

                var pantalla by rememberSaveable { mutableStateOf("inicio") }

                // Perfil con el que se ingresó: define a qué panel se vuelve
                var perfil by rememberSaveable { mutableStateOf("operario") }

                // Botón "atrás" del celular: vuelve a la pantalla anterior
                BackHandler(enabled = pantalla != "inicio") {
                    pantalla = when (pantalla) {
                        "login" -> "inicio"
                        "roles" -> "login"
                        "operario", "supervisor" -> "roles"
                        else -> perfil // temperaturas, alertas y flushing
                    }
                }

                when (pantalla) {
                    "inicio" -> HomeScreen {
                        pantalla = "login"
                    }

                    "login" -> LoginScreen(
                        onLoginExitoso = { pantalla = "roles" },
                        onVolver = { pantalla = "inicio" }
                    )

                    "roles" -> RolSelectionScreen(
                        onOperario = {
                            perfil = "operario"
                            pantalla = "operario"
                        },
                        onSupervisor = {
                            perfil = "supervisor"
                            pantalla = "supervisor"
                        }
                    )

                    "operario" -> OperarioScreen(
                        onTemperaturas = { pantalla = "temperaturas" },
                        onFlushing = { pantalla = "flushing" },
                        onAlertas = { pantalla = "alertas" },
                        onInicio = { pantalla = "inicio" }
                    )

                    "supervisor" -> SupervisorScreen(
                        lineas = lineas,
                        registros = registros,
                        onTemperaturas = { pantalla = "temperaturas" },
                        onAlertas = { pantalla = "alertas" },
                        onFlushing = { pantalla = "flushing" },
                        onInicio = { pantalla = "inicio" }
                    )

                    "temperaturas" -> TemperaturasScreen(
                        lineas = lineas,
                        onGuardar = { granja, galpon, linea, temperatura ->
                            bebederoViewModel.guardarTemperatura(
                                granja,
                                galpon,
                                linea,
                                temperatura
                            )
                        },
                        onVolver = { pantalla = perfil },
                        clima = clima,
                        onGranjaCambiada = { bebederoViewModel.cargarClima(it) }
                    )

                    "alertas" -> AlertasScreen(
                        lineas = lineas,
                        onVolver = { pantalla = perfil }
                    )

                    "flushing" -> FlusingScreen(
                        registros = registros,
                        onGuardar = { registro ->
                            mainViewModel.guardarRegistro(registro)
                        },
                        onEliminar = { id ->
                            mainViewModel.eliminarRegistro(id)
                        },
                        onActualizarObservacion = { registro, nuevaObservacion ->
                            mainViewModel.actualizarObservacion(registro, nuevaObservacion)
                        },
                        onVolver = { pantalla = perfil }
                    )

                    else -> HomeScreen {
                        pantalla = "login"
                    }
                }
            }
        }
    }
}