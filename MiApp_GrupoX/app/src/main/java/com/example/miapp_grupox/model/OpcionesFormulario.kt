package com.example.miapp_grupox.model

// Opciones fijas que se muestran en los combo box de los formularios
object OpcionesBebedero {
    val granjas = listOf("Granja Melipilla", "Granja Santiago", "Granja Rancagua")
    val galpones = listOf("Galpón 1", "Galpón 2", "Galpón 3", "Galpón 4")
    val lineas = listOf("Línea 1", "Línea 2", "Línea 3", "Línea 4")
    // Coordenadas (latitud, longitud) de cada granja: se usan para consultar el clima en la API externa
    val coordenadas = mapOf(
        "Granja Melipilla" to Pair(-33.69, -71.21),
        "Granja Santiago" to Pair(-33.45, -70.66),
        "Granja Rancagua" to Pair(-34.17, -70.74)
    )
    val motivosFlushing = listOf(
        "Limpieza preventiva",
        "Temperatura elevada",
        "Mantenimiento",
        "Alerta sanitaria",
        "Otro"
    )
}

// Un rango de temperatura que el operario puede elegir en el formulario
data class RangoTemperatura(
    val etiqueta: String,             // texto que ve el usuario
    val valorRepresentativo: Double   // valor que se guarda en la base de datos
)

// Lógica de rangos (sin interfaz). Coincide con los límites de estado de LineBebedero:
// desde 27 °C es Advertencia y desde 30 °C es Crítico
object RangosTemperatura {

    val lista = listOf(
        RangoTemperatura("Menos de 20 °C", 18.0),
        RangoTemperatura("20 °C a 26,9 °C", 23.0),
        RangoTemperatura("27 °C a 29,9 °C", 28.5),
        RangoTemperatura("30 °C o más", 32.0)
    )

    val etiquetas: List<String> = lista.map { it.etiqueta }

    // Valor que se guarda según el rango elegido (null si no eligió ninguno)
    fun valorDe(etiqueta: String): Double? {
        return lista.firstOrNull { it.etiqueta == etiqueta }?.valorRepresentativo
    }

    // Convierte una temperatura guardada en el rango al que pertenece
    fun etiquetaDe(temperatura: Double): String {
        return when {
            temperatura < 20.0 -> lista[0].etiqueta
            temperatura < 27.0 -> lista[1].etiqueta
            temperatura < 30.0 -> lista[2].etiqueta
            else -> lista[3].etiqueta
        }
    }
}