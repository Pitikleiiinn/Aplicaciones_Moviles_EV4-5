package com.example.miapp_grupox.remote

/**
 * [CONFIGURACIÓN DE RED] Direcciones de los microservicios y de la API externa.
 *
 *  - Emulador de Android Studio: el PC se ve como 10.0.2.2 (localhost NO funciona dentro del emulador).
 *  - Celular físico: cambia HOST por la IP de tu PC en la red WiFi, ej. "http://192.168.1.50".
 */
object ApiConfig {
    const val HOST = "http://10.0.2.2"

    const val URL_TEMPERATURAS = "$HOST:8082/"   // ms-temperaturas
    const val URL_FLUSHING = "$HOST:8083/"       // ms-flushing
    const val URL_USUARIOS = "$HOST:8084/"       // ms-usuarios

    /** API EXTERNA (terceros): clima actual por coordenadas, sin API key */
    const val URL_CLIMA = "https://api.open-meteo.com/"
}
