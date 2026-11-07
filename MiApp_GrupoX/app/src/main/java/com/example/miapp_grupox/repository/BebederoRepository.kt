
package com.example.miapp_grupox.repository

import com.example.miapp_grupox.model.BebederoDao
import com.example.miapp_grupox.model.LineBebedero
import com.example.miapp_grupox.remote.LineasApi
import com.example.miapp_grupox.remote.Resultado
import com.example.miapp_grupox.remote.RetrofitClient
import com.example.miapp_grupox.remote.TemperaturaRequest
import com.example.miapp_grupox.remote.llamadaSegura
import kotlinx.coroutines.flow.Flow

/**
 * Las líneas viven en el microservicio ms-temperaturas (fuente de verdad).
 * Room queda como copia local: la pantalla sigue leyendo de Room (Flow) y este repositorio
 * mantiene esa copia al día con lo que responde el servidor.
 * Los métodos de red devuelven null si todo salió bien, o el mensaje de error para mostrar.
 */
class BebederoRepository(
    private val dao: BebederoDao,
    private val api: LineasApi = RetrofitClient.lineasApi
) {
    fun obtenerLineas(): Flow<List<LineBebedero>> =
        dao.obtenerTodas()

    suspend fun guardar(linea: LineBebedero) =
        dao.guardar(linea)

    // READ: descarga las líneas del servidor y actualiza la copia local
    suspend fun sincronizar(): String? =
        when (val r = llamadaSegura { api.listar() }) {
            is Resultado.Exito -> {
                dao.guardarTodas(r.datos)
                dao.borrarFueraDe(r.datos.map { it.id })
                null
            }
            is Resultado.Error -> r.mensaje
        }

    // CREATE / UPDATE: el servidor crea o actualiza la línea y devuelve el estado oficial
    suspend fun guardarTemperatura(
        granja: String,
        galpon: String,
        linea: String,
        temperatura: Double
    ): String? =
        when (val r = llamadaSegura {
            api.registrarTemperatura(TemperaturaRequest(granja, galpon, linea, temperatura))
        }) {
            is Resultado.Exito -> {
                dao.guardar(r.datos)
                null
            }
            is Resultado.Error -> r.mensaje
        }

    // Al abrir la app: sincroniza con el servidor. Si no hay conexión y no hay datos locales,
    // carga los datos de demostración para que la app no quede vacía.
    suspend fun inicializarDatos(): String? {
        val error = sincronizar()
        if (error != null && dao.contar() == 0) {
            dao.guardar(
                LineBebedero(
                    granja = "Granja Melipilla",
                    galpon = "Galpón 1",
                    linea = "Línea 1",
                    temperatura = 22.5,
                    fecha = "Datos de demostración"
                )
            )
            dao.guardar(
                LineBebedero(
                    granja = "Granja Melipilla",
                    galpon = "Galpón 1",
                    linea = "Línea 2",
                    temperatura = 28.0,
                    fecha = "Datos de demostración"
                )
            )
            dao.guardar(
                LineBebedero(
                    granja = "Granja Melipilla",
                    galpon = "Galpón 2",
                    linea = "Línea 1",
                    temperatura = 31.5,
                    fecha = "Datos de demostración"
                )
            )
        }
        return error
    }
}
