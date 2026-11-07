package com.example.miapp_grupox.model



// Resultado de validar el login: errores por campo y mensaje general
data class ResultadoLogin(
    val errorUsuario: String? = null,
    val errorClave: String? = null,
    val mensaje: String? = null,
    val exitoso: Boolean = false
)

// Lógica del login, separada de la interfaz
object LoginValidator {

    // Usuarios ficticios para el MVP académico (usuario -> contraseña)
    private val usuarios = mapOf(
        "operario" to "1234",
        "supervisor" to "1234"
    )

    // Valida SOLO el formato de los campos. Las credenciales las verifica el servidor (ms-usuarios).
    fun validarCampos(usuario: String, clave: String): ResultadoLogin {
        val u = usuario.trim()
        val errorUsuario = when {
            u.isBlank() -> "Ingresa tu usuario"
            u.length < 4 -> "El usuario debe tener al menos 4 caracteres"
            else -> null
        }
        val errorClave = when {
            clave.isBlank() -> "Ingresa tu contraseña"
            clave.length < 4 -> "La contraseña debe tener al menos 4 caracteres"
            else -> null
        }
        val hayErrores = errorUsuario != null || errorClave != null
        return ResultadoLogin(
            errorUsuario = errorUsuario,
            errorClave = errorClave,
            mensaje = if (hayErrores) "Revisa los campos marcados" else null,
            exitoso = !hayErrores
        )
    }

    // Verificación local con usuarios ficticios (versión del Parcial 2, ya no se usa en la pantalla)
    fun validar(usuario: String, clave: String): ResultadoLogin {
        val u = usuario.trim().lowercase()

        val errorUsuario = when {
            u.isBlank() -> "Ingresa tu usuario"
            u.length < 4 -> "El usuario debe tener al menos 4 caracteres"
            else -> null
        }

        val errorClave = when {
            clave.isBlank() -> "Ingresa tu contraseña"
            clave.length < 4 -> "La contraseña debe tener al menos 4 caracteres"
            else -> null
        }

        if (errorUsuario != null || errorClave != null) {
            return ResultadoLogin(
                errorUsuario = errorUsuario,
                errorClave = errorClave,
                mensaje = "Revisa los campos marcados"
            )
        }

        return if (usuarios[u] == clave) {
            ResultadoLogin(exitoso = true)
        } else {
            ResultadoLogin(mensaje = "Usuario o contraseña incorrectos")
        }
    }
}