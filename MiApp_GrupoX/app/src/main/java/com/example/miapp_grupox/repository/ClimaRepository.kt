package com.example.miapp_grupox.repository

import com.example.miapp_grupox.remote.ClimaActual
import com.example.miapp_grupox.remote.ClimaApi
import com.example.miapp_grupox.remote.Resultado
import com.example.miapp_grupox.remote.RetrofitClient
import com.example.miapp_grupox.remote.llamadaSegura

/** [REPOSITORIO] Acceso a la API EXTERNA (Open-Meteo): clima actual de una granja. */
class ClimaRepository(private val api: ClimaApi = RetrofitClient.climaApi) {

    companion object {
        const val CAMPOS = "temperature_2m,relative_humidity_2m"
    }

    suspend fun actual(latitud: Double, longitud: Double): Resultado<ClimaActual> =
        when (val r = llamadaSegura { api.climaActual(latitud, longitud, CAMPOS) }) {
            is Resultado.Exito -> r.datos.current?.let { Resultado.Exito(it) } ?: Resultado.Error("Clima no disponible")
            is Resultado.Error -> r
        }
}
