package cl.ariztia.bebederos.ui.navigation

/** [NAVEGACIÓN] Nombres de las rutas (destinos). Las que llevan {id} reciben un argumento. */
object Rutas {
    const val LOGIN = "login"
    const val HOME = "home"
    const val GRANJAS = "granjas"
    const val ALERTAS = "alertas"
    const val FLUSHING = "flushing"
    const val HISTORIAL = "historial"

    const val GALPONES = "galpones/{granjaId}"
    const val LINEAS = "lineas/{galponId}"
    const val DETALLE = "detalle/{lineaId}"
    const val TEMPERATURA = "temperatura/{lineaId}"

    fun galpones(granjaId: Long) = "galpones/$granjaId"
    fun lineas(galponId: Long) = "lineas/$galponId"
    fun detalle(lineaId: Long) = "detalle/$lineaId"
    fun temperatura(lineaId: Long) = "temperatura/$lineaId"
}
