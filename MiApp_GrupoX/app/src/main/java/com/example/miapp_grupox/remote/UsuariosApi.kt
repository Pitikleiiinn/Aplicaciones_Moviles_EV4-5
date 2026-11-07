package com.example.miapp_grupox.remote

import retrofit2.http.Body
import retrofit2.http.POST

/** [RETROFIT] Microservicio ms-usuarios (puerto 8084). */
interface UsuariosApi {
    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): UsuarioDto
}
