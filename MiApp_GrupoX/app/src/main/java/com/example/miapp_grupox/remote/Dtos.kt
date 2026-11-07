package com.example.miapp_grupox.remote

import com.google.gson.annotations.SerializedName

/** [DTO] Cuerpo para registrar una temperatura en ms-temperaturas (el estado lo calcula el servidor). */
data class TemperaturaRequest(
    val granja: String,
    val galpon: String,
    val linea: String,
    val temperatura: Double
)

/** [DTO] Cuerpo para crear o corregir un flushing en ms-flushing. */
data class FlushingRequest(
    val granja: String,
    val galpon: String,
    val linea: String,
    val responsable: String,
    val motivo: String,
    val observaciones: String
)

/** [DTO] Cuerpo del login en ms-usuarios. */
data class LoginRequest(val usuario: String, val clave: String)

/** [DTO] Respuesta del login: datos del usuario y su rol (OPERARIO / SUPERVISOR). */
data class UsuarioDto(val id: Long, val usuario: String, val nombre: String, val rol: String)

/**
 * [DTO API EXTERNA] Respuesta de Open-Meteo (https://open-meteo.com):
 * { "current": { "temperature_2m": 18.3, "relative_humidity_2m": 60 } }
 */
data class ClimaResponse(val current: ClimaActual?)

data class ClimaActual(
    @SerializedName("temperature_2m") val temperatura: Double,
    @SerializedName("relative_humidity_2m") val humedad: Int
)
