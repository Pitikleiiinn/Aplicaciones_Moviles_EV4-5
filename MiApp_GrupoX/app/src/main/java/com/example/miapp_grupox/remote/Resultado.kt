package com.example.miapp_grupox.remote

import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

/** [UTIL] Resultado de una llamada de red: éxito con datos, o error con un mensaje listo para mostrar. */
sealed interface Resultado<out T> {
    data class Exito<T>(val datos: T) : Resultado<T>
    data class Error(val mensaje: String) : Resultado<Nothing>
}

/**
 * [UTIL] Ejecuta una llamada de red y traduce los errores típicos a mensajes entendibles:
 *  - HttpException: el servidor respondió con error (400, 401, 404...). Se lee el {"error": "..."} del backend.
 *  - IOException: no hay conexión o el servidor está apagado.
 */
suspend fun <T> llamadaSegura(bloque: suspend () -> T): Resultado<T> =
    try {
        Resultado.Exito(bloque())
    } catch (e: CancellationException) {
        throw e // nunca tragarse la cancelación de corrutinas
    } catch (e: HttpException) {
        val cuerpo = try { e.response()?.errorBody()?.string() } catch (_: Exception) { null }
        Resultado.Error(extraerMensajeServidor(cuerpo) ?: "Error del servidor (${e.code()})")
    } catch (e: IOException) {
        Resultado.Error("Sin conexión con el servidor. Revisa que los microservicios estén encendidos.")
    } catch (e: Exception) {
        Resultado.Error(e.message ?: "Ocurrió un error inesperado")
    }

/** Saca el texto de {"error":"mensaje"} que devuelve el GlobalExceptionHandler del backend. */
fun extraerMensajeServidor(cuerpo: String?): String? {
    if (cuerpo.isNullOrBlank()) return null
    return Regex("\"error\"\\s*:\\s*\"([^\"]*)\"").find(cuerpo)?.groupValues?.get(1)
}
