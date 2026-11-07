package com.example.miapp_grupox.remote

import com.example.miapp_grupox.model.LineBebedero
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * [RETROFIT] Microservicio ms-temperaturas (puerto 8082).
 * Devuelve directamente LineBebedero porque el JSON del servidor usa los mismos nombres de campo.
 */
interface LineasApi {
    @GET("api/lineas")
    suspend fun listar(): List<LineBebedero>

    /** Crea o actualiza la línea (granja + galpón + línea) con la temperatura indicada */
    @POST("api/lineas/temperatura")
    suspend fun registrarTemperatura(@Body request: TemperaturaRequest): LineBebedero
}
