package cl.ariztia.bebederos

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.ariztia.bebederos.ui.navigation.AppNavHost
import cl.ariztia.bebederos.ui.theme.AriztiaTheme
import cl.ariztia.bebederos.viewmodel.SesionViewModel
import cl.ariztia.bebederos.viewmodel.fabrica

/**
 * [ACTIVITY] Única Activity de la app (patrón single-activity con Compose).
 * Pide el permiso de notificaciones (Android 13+) y monta el tema + la navegación.
 */
class MainActivity : ComponentActivity() {

    // Lanzador del diálogo de permiso del sistema
    private val pedirPermisoNotificaciones =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* si lo rechaza, simplemente no hay notificaciones */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            pedirPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            AriztiaTheme {
                // El SesionViewModel vive en la Activity: se comparte entre todas las pantallas
                val sesionVm: SesionViewModel = viewModel(factory = fabrica { SesionViewModel(AppContainer.usuarios) })
                AppNavHost(sesionVm)
            }
        }
    }
}
