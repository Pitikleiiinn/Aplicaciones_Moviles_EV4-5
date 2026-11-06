package cl.ariztia.bebederos.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/** [UTIL] Formatea fechas ISO del backend a un formato legible para el usuario. */
object FechaUtil {
    private val salida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    fun formatear(iso: String): String =
        try {
            LocalDateTime.parse(iso).format(salida)
        } catch (e: Exception) {
            iso // si no se puede interpretar, se muestra tal cual
        }
}
