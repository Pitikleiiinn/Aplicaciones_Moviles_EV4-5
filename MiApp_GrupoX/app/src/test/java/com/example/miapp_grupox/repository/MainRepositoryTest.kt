package com.example.miapp_grupox.repository

import com.example.miapp_grupox.model.FlusingDao
import com.example.miapp_grupox.model.FlusingEntity
import com.example.miapp_grupox.remote.FlushingApi
import com.example.miapp_grupox.remote.FlushingRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** MainRepository (flushing): crear, actualizar, eliminar y sincronizar con API y DAO simulados. */
class MainRepositoryTest {
    private val dao = mockk<FlusingDao>(relaxUnitFun = true)
    private val api = mockk<FlushingApi>()
    private val repo = MainRepository(dao, api)

    private val local = FlusingEntity(0, "Granja Melipilla", "Galpón 1", "Línea 1", "Carlos Soto", "Mantenimiento", "Todo bien", "01/01/2026 10:00")
    private val delServidor = local.copy(id = 7, fecha = "05/11/2026 09:30")
    private val solicitud = FlushingRequest("Granja Melipilla", "Galpón 1", "Línea 1", "Carlos Soto", "Mantenimiento", "Todo bien")

    @Test fun `crear envia la solicitud sin id ni fecha y guarda la respuesta`() = runTest {
        coEvery { api.crear(solicitud) } returns delServidor
        assertNull(repo.guardarRegistro(local))
        coVerify { dao.insertar(delServidor) }
    }

    @Test fun `crear con error no guarda localmente`() = runTest {
        coEvery { api.crear(any()) } throws IOException("sin red")
        assertTrue(repo.guardarRegistro(local)!!.startsWith("Sin conexión"))
        coVerify(exactly = 0) { dao.insertar(any()) }
    }

    @Test fun `actualizar usa el id del registro`() = runTest {
        val editado = delServidor.copy(observaciones = "Corregido")
        coEvery { api.actualizar(7L, solicitud.copy(observaciones = "Corregido")) } returns editado
        assertNull(repo.actualizarRegistro(delServidor.copy(observaciones = "Corregido")))
        coVerify { dao.actualizar(editado) }
    }

    @Test fun `eliminar borra en el servidor y luego en Room`() = runTest {
        coEvery { api.eliminar(7L) } returns Unit
        assertNull(repo.eliminarRegistro(7))
        coVerify { dao.eliminar(7) }
    }

    @Test fun `eliminar con error conserva el registro local`() = runTest {
        coEvery { api.eliminar(any()) } throws IOException("sin red")
        assertTrue(repo.eliminarRegistro(7)!!.startsWith("Sin conexión"))
        coVerify(exactly = 0) { dao.eliminar(any()) }
    }

    @Test fun `sincronizar reemplaza el historial local`() = runTest {
        coEvery { api.listar() } returns listOf(delServidor)
        assertNull(repo.sincronizar())
        coVerify { dao.insertarTodos(listOf(delServidor)) }
        coVerify { dao.borrarFueraDe(listOf(7)) }
    }

    @Test fun `sincronizar sin conexion devuelve mensaje`() = runTest {
        coEvery { api.listar() } throws IOException("sin red")
        assertTrue(repo.sincronizar()!!.startsWith("Sin conexión"))
    }
}
