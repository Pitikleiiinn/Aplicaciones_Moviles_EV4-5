
package com.example.miapp_grupox.repository

import com.example.miapp_grupox.model.FlusingDao
import com.example.miapp_grupox.model.FlusingEntity
import com.example.miapp_grupox.remote.FlushingApi
import com.example.miapp_grupox.remote.FlushingRequest
import com.example.miapp_grupox.remote.Resultado
import com.example.miapp_grupox.remote.RetrofitClient
import com.example.miapp_grupox.remote.llamadaSegura
import kotlinx.coroutines.flow.Flow

/**
 * Los registros de flushing viven en el microservicio ms-flushing (fuente de verdad).
 * Room queda como copia local para que la pantalla siga leyendo de un Flow.
 * Los métodos de escritura devuelven null si salió bien, o el mensaje de error para mostrar.
 */
class MainRepository(
    private val dao: FlusingDao,
    private val api: FlushingApi = RetrofitClient.flushingApi
) {
    fun obtenerRegistros(): Flow<List<FlusingEntity>> =
        dao.obtenerTodos()

    // READ: descarga el historial del servidor y actualiza la copia local
    suspend fun sincronizar(): String? =
        when (val r = llamadaSegura { api.listar() }) {
            is Resultado.Exito -> {
                dao.insertarTodos(r.datos)
                dao.borrarFueraDe(r.datos.map { it.id })
                null
            }
            is Resultado.Error -> r.mensaje
        }

    // CREATE
    suspend fun guardarRegistro(registro: FlusingEntity): String? =
        when (val r = llamadaSegura { api.crear(aSolicitud(registro)) }) {
            is Resultado.Exito -> {
                dao.insertar(r.datos)
                null
            }
            is Resultado.Error -> r.mensaje
        }

    // UPDATE
    suspend fun actualizarRegistro(registro: FlusingEntity): String? =
        when (val r = llamadaSegura { api.actualizar(registro.id.toLong(), aSolicitud(registro)) }) {
            is Resultado.Exito -> {
                dao.actualizar(r.datos)
                null
            }
            is Resultado.Error -> r.mensaje
        }

    // DELETE
    suspend fun eliminarRegistro(id: Int): String? =
        when (val r = llamadaSegura { api.eliminar(id.toLong()) }) {
            is Resultado.Exito -> {
                dao.eliminar(id)
                null
            }
            is Resultado.Error -> r.mensaje
        }

    // La fecha y el id los asigna el servidor, por eso no viajan en la solicitud
    private fun aSolicitud(r: FlusingEntity) = FlushingRequest(
        granja = r.granja,
        galpon = r.galpon,
        linea = r.linea,
        responsable = r.responsable,
        motivo = r.motivo,
        observaciones = r.observaciones
    )
}
