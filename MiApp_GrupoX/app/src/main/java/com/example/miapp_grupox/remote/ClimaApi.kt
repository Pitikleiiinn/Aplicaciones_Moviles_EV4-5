package com.example.miapp_grupox.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * [RETROFIT - API EXTERNA] Open-Meteo.
 * DIFERENCIA con un microservicio propio: aquí NO controlamos el servidor ni el formato de respuesta,
 * no hay base de datos nuestra y solo leemos datos de terceros (no se puede crear ni modificar nada).
 */
interface ClimaApi {
    @GET("v1/forecast")
    suspend fun climaActual(
        @Query("latitude") latitud: Double,
        @Query("longitude") longitud: Double,
        @Query("current") campos: String
    ): ClimaResponse
}
