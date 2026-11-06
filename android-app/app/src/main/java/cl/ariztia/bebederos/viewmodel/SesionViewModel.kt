package cl.ariztia.bebederos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.ariztia.bebederos.data.model.Usuario
import cl.ariztia.bebederos.data.repository.UsuariosRepository
import cl.ariztia.bebederos.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** [VIEWMODEL] Sesión del usuario (login / logout). Se comparte en toda la app (vive en la Activity). */
class SesionViewModel(private val repo: UsuariosRepository) : ViewModel() {

    /** Estado del formulario de login */
    data class LoginUi(val cargando: Boolean = false, val error: String? = null)

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    private val _ui = MutableStateFlow(LoginUi())
    val ui: StateFlow<LoginUi> = _ui.asStateFlow()

    fun iniciarSesion(email: String, password: String) {
        // [VALIDACIÓN] Antes de molestar al servidor
        if (email.isBlank() || password.isBlank()) {
            _ui.value = LoginUi(error = "Ingresa tu correo y contraseña")
            return
        }
        viewModelScope.launch {
            _ui.value = LoginUi(cargando = true)
            when (val r = repo.login(email.trim(), password)) {
                is Resultado.Exito -> {
                    _usuario.value = r.datos
                    _ui.value = LoginUi()
                }
                is Resultado.Error -> _ui.value = LoginUi(error = r.mensaje)
            }
        }
    }

    fun cerrarSesion() {
        _usuario.value = null
    }
}
