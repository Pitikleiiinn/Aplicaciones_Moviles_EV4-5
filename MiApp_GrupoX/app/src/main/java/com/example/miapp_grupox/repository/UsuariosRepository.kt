package com.example.miapp_grupox.repository

import com.example.miapp_grupox.remote.LoginRequest
import com.example.miapp_grupox.remote.Resultado
import com.example.miapp_grupox.remote.RetrofitClient
import com.example.miapp_grupox.remote.UsuariosApi
import com.example.miapp_grupox.remote.UsuarioDto
import com.example.miapp_grupox.remote.llamadaSegura

/** [REPOSITORIO] Acceso a ms-usuarios: valida las credenciales en el servidor. */
class UsuariosRepository(private val api: UsuariosApi = RetrofitClient.usuariosApi) {
    suspend fun login(usuario: String, clave: String): Resultado<UsuarioDto> =
        llamadaSegura { api.login(LoginRequest(usuario, clave)) }
}
