package com.example.miapp_grupox.repository

import com.example.miapp_grupox.model.BebederoDao
import com.example.miapp_grupox.model.LineBebedero
import com.example.miapp_grupox.remote.LineasApi
import com.example.miapp_grupox.remote.TemperaturaRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * BebederoRepository con DAO y API simulados (mock): se prueba la lógica de sincronización,
 * no la red real ni la base de datos real.
 */
class BebederoRepositoryTest {
    private val dao = mockk<BebederoDao>(relaxUnitFun = true)
    private val api = mockk<LineasApi>()
    private val repo = BebederoRepository(dao, api)

    private val l1 = LineBebedero(1, "Granja Melipilla", "Galpón 1", "Línea 1", 22.5, "01/01/2026 10:00")
    private val l2 = LineBebedero(2, "Granja Melipilla", "Galpón 1", "Línea 2", 28.0, "01/01/2026 10:00")

    @Test fun `sincronizar guarda lo recibido y borra lo que ya no existe`() = runTest {
        coEvery { api.listar() } returns listOf(l1, l2)
        assertNull(repo.sincronizar())
        coVerify { dao.guardarTodas(listOf(l1, l2)) }
        coVerify { dao.borrarFueraDe(listOf(1, 2)) }
    }

    @Test fun `sincronizar sin conexion devuelve el mensaje y no toca Room`() = runTest {
        coEvery { api.listar() } throws IOException("sin red")
        val error = repo.sincronizar()
        assertTrue(error!!.startsWith("Sin conexión"))
        coVerify(exactly = 0) { dao.guardarTodas(any()) }
    }

    @Test fun `guardarTemperatura envia la solicitud y guarda la respuesta del servidor`() = runTest {
        coEvery { api.registrarTemperatura(TemperaturaRequest("Granja Melipilla", "Galpón 1", "Línea 1", 31.5)) } returns l1
        assertNull(repo.guardarTemperatura("Granja Melipilla", "Galpón 1", "Línea 1", 31.5))
        coVerify { dao.guardar(l1) }
    }

    @Test fun `guardarTemperatura con error no guarda localmente`() = runTest {
        coEvery { api.registrarTemperatura(any()) } throws IOException("sin red")
        assertTrue(repo.guardarTemperatura("G", "Ga", "L", 20.0)!!.startsWith("Sin conexión"))
        coVerify(exactly = 0) { dao.guardar(any()) }
    }

    @Test fun `inicializar sin conexion y sin datos carga la demostracion`() = runTest {
        coEvery { api.listar() } throws IOException("sin red")
        coEvery { dao.contar() } returns 0
        repo.inicializarDatos()
        coVerify(exactly = 3) { dao.guardar(any()) }
    }

    @Test fun `inicializar sin conexion pero con datos no pisa nada`() = runTest {
        coEvery { api.listar() } throws IOException("sin red")
        coEvery { dao.contar() } returns 5
        repo.inicializarDatos()
        coVerify(exactly = 0) { dao.guardar(any()) }
    }

    @Test fun `inicializar con conexion no carga demostracion`() = runTest {
        coEvery { api.listar() } returns listOf(l1)
        assertNull(repo.inicializarDatos())
        coVerify(exactly = 0) { dao.guardar(any()) }
    }
}
