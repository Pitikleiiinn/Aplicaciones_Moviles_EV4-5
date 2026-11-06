package cl.ariztia.bebederos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.ariztia.bebederos.AppContainer
import cl.ariztia.bebederos.ui.screens.AlertasScreen
import cl.ariztia.bebederos.ui.screens.DetalleLineaScreen
import cl.ariztia.bebederos.ui.screens.FlushingFormScreen
import cl.ariztia.bebederos.ui.screens.GalponesScreen
import cl.ariztia.bebederos.ui.screens.GranjasScreen
import cl.ariztia.bebederos.ui.screens.HistorialFlushingScreen
import cl.ariztia.bebederos.ui.screens.HomeScreen
import cl.ariztia.bebederos.ui.screens.LineasScreen
import cl.ariztia.bebederos.ui.screens.LoginScreen
import cl.ariztia.bebederos.ui.screens.RegistrarTemperaturaScreen
import cl.ariztia.bebederos.viewmodel.AlertasViewModel
import cl.ariztia.bebederos.viewmodel.DetalleLineaViewModel
import cl.ariztia.bebederos.viewmodel.FlushingFormViewModel
import cl.ariztia.bebederos.viewmodel.GalponesViewModel
import cl.ariztia.bebederos.viewmodel.GranjasViewModel
import cl.ariztia.bebederos.viewmodel.HistorialFlushingViewModel
import cl.ariztia.bebederos.viewmodel.LineasViewModel
import cl.ariztia.bebederos.viewmodel.RegistrarTemperaturaViewModel
import cl.ariztia.bebederos.viewmodel.SesionViewModel
import cl.ariztia.bebederos.viewmodel.fabrica

/** Lee un argumento Long de la ruta (ej. el {granjaId} de "galpones/{granjaId}") */
private fun NavBackStackEntry.id(nombre: String): Long = arguments?.getLong(nombre) ?: 0L

private fun argLong(nombre: String) = listOf(navArgument(nombre) { type = NavType.LongType })

/**
 * [NAVEGACIÓN] Grafo de la app (Navigation Compose).
 * Flujo: Login → Home → Granjas → Galpones → Líneas → Detalle → (Registrar temperatura | Registrar flushing)
 * Cada destino crea SU ViewModel con los repositorios del AppContainer.
 */
@Composable
fun AppNavHost(sesionVm: SesionViewModel, nav: NavHostController = rememberNavController()) {
    val usuario by sesionVm.usuario.collectAsStateWithLifecycle()

    NavHost(navController = nav, startDestination = Rutas.LOGIN) {

        composable(Rutas.LOGIN) {
            LoginScreen(sesionVm, onIngreso = {
                nav.navigate(Rutas.HOME) { popUpTo(Rutas.LOGIN) { inclusive = true } }
            })
        }

        composable(Rutas.HOME) {
            val alertasVm: AlertasViewModel = viewModel(key = "alertas_home", factory = fabrica { AlertasViewModel(AppContainer.temperaturas) })
            HomeScreen(
                usuario = usuario, alertasVm = alertasVm,
                onGranjas = { nav.navigate(Rutas.GRANJAS) },
                onFlushing = { nav.navigate(Rutas.FLUSHING) },
                onHistorial = { nav.navigate(Rutas.HISTORIAL) },
                onAlertas = { nav.navigate(Rutas.ALERTAS) },
                onSalir = {
                    sesionVm.cerrarSesion()
                    nav.navigate(Rutas.LOGIN) { popUpTo(0) }
                }
            )
        }

        composable(Rutas.GRANJAS) {
            val vm: GranjasViewModel = viewModel(factory = fabrica { GranjasViewModel(AppContainer.granjas) })
            GranjasScreen(vm, onVolver = { nav.popBackStack() }, onGranja = { nav.navigate(Rutas.galpones(it)) })
        }

        composable(Rutas.GALPONES, arguments = argLong("granjaId")) { entry ->
            val granjaId = entry.id("granjaId")
            val vm: GalponesViewModel = viewModel(key = "galpones_$granjaId", factory = fabrica { GalponesViewModel(AppContainer.granjas, granjaId) })
            GalponesScreen(vm, onVolver = { nav.popBackStack() }, onGalpon = { nav.navigate(Rutas.lineas(it)) })
        }

        composable(Rutas.LINEAS, arguments = argLong("galponId")) { entry ->
            val galponId = entry.id("galponId")
            val vm: LineasViewModel = viewModel(key = "lineas_$galponId",
                factory = fabrica { LineasViewModel(AppContainer.granjas, AppContainer.temperaturas, galponId) })
            LineasScreen(vm, onVolver = { nav.popBackStack() }, onLinea = { nav.navigate(Rutas.detalle(it)) })
        }

        composable(Rutas.DETALLE, arguments = argLong("lineaId")) { entry ->
            val lineaId = entry.id("lineaId")
            val vm: DetalleLineaViewModel = viewModel(key = "detalle_$lineaId",
                factory = fabrica { DetalleLineaViewModel(AppContainer.granjas, AppContainer.temperaturas, AppContainer.clima, lineaId) })
            DetalleLineaScreen(
                vm = vm, rol = usuario?.rol,
                onVolver = { nav.popBackStack() },
                onRegistrarTemperatura = { nav.navigate(Rutas.temperatura(lineaId)) },
                onRegistrarFlushing = { nav.navigate(Rutas.FLUSHING) }
            )
        }

        composable(Rutas.TEMPERATURA, arguments = argLong("lineaId")) { entry ->
            val lineaId = entry.id("lineaId")
            val vm: RegistrarTemperaturaViewModel = viewModel(key = "reg_temp_$lineaId",
                factory = fabrica { RegistrarTemperaturaViewModel(AppContainer.granjas, AppContainer.temperaturas, lineaId) })
            RegistrarTemperaturaScreen(vm, onVolver = { nav.popBackStack() })
        }

        composable(Rutas.FLUSHING) {
            val vm: FlushingFormViewModel = viewModel(
                factory = fabrica { FlushingFormViewModel(AppContainer.granjas, AppContainer.flushing, usuario?.nombre ?: "") })
            FlushingFormScreen(vm, onVolver = { nav.popBackStack() })
        }

        composable(Rutas.HISTORIAL) {
            val vm: HistorialFlushingViewModel = viewModel(factory = fabrica { HistorialFlushingViewModel(AppContainer.flushing) })
            HistorialFlushingScreen(vm, onVolver = { nav.popBackStack() })
        }

        composable(Rutas.ALERTAS) {
            val vm: AlertasViewModel = viewModel(key = "alertas_lista", factory = fabrica { AlertasViewModel(AppContainer.temperaturas) })
            AlertasScreen(vm, onVolver = { nav.popBackStack() })
        }
    }
}
