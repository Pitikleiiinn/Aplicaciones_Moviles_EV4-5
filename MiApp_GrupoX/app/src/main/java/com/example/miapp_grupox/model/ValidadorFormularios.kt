package com.example.miapp_grupox.model

// Resultado de una validación: errores por campo (clave = nombre del campo)
data class ResultadoValidacion(
    val errores: Map<String, String> = emptyMap()
) {
    val valido: Boolean
        get() = errores.isEmpty()
}

// Validación del formulario de temperaturas (lógica separada de la interfaz)
object ValidadorTemperatura {

    fun validar(rango: String): ResultadoValidacion {
        val errores = mutableMapOf<String, String>()

        if (rango.isBlank() || RangosTemperatura.valorDe(rango) == null) {
            errores["rango"] = "Selecciona un rango de temperatura"
        }

        return ResultadoValidacion(errores)
    }
}

// Validación del formulario de flushing (lógica separada de la interfaz)
object ValidadorFlushing {

    private val soloLetras = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+$")

    // Devuelve el mensaje de error o null si la observación es válida
    fun validarObservacion(observaciones: String): String? {
        val obs = observaciones.trim()
        return when {
            obs.isBlank() -> "Escribe una observación"
            obs.length < 5 -> "La observación debe tener al menos 5 caracteres"
            else -> null
        }
    }

    fun validar(
        responsable: String,
        motivo: String,
        observaciones: String,
        tieneFoto: Boolean
    ): ResultadoValidacion {
        val errores = mutableMapOf<String, String>()

        val nombre = responsable.trim()
        when {
            nombre.isBlank() ->
                errores["responsable"] = "Ingresa el nombre del responsable"
            nombre.length < 3 ->
                errores["responsable"] = "El nombre debe tener al menos 3 letras"
            !soloLetras.matches(nombre) ->
                errores["responsable"] = "El nombre solo puede tener letras"
        }

        if (motivo.isBlank()) {
            errores["motivo"] = "Selecciona el motivo del flushing"
        }

        validarObservacion(observaciones)?.let { mensaje ->
            errores["observaciones"] = mensaje
        }

        if (!tieneFoto) {
            errores["foto"] = "Falta la foto de evidencia"
        }

        return ResultadoValidacion(errores)
    }
}