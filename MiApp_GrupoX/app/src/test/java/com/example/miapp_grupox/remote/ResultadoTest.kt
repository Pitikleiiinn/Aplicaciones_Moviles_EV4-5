package com.example.miapp_grupox.remote

import java.io.IOException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/** Manejo de errores de red: cómo se traducen las excepciones a mensajes para el usuario. */
class ResultadoTest {

    @Test fun `extrae el mensaje de error del backend`() =
        assertEquals("Usuario o contraseña incorrectos", extraerMensajeServidor("""{"error":"Usuario o contraseña incorrectos"}"""))

    @Test fun `devuelve null si no hay error en el cuerpo`() {
        assertNull(extraerMensajeServidor(null))
        assertNull(extraerMensajeServidor("""{"otro":"x"}"""))
    }

    @Test fun `llamada exitosa entrega Exito`() = runTest {
        assertEquals(Resultado.Exito(42), llamadaSegura { 42 })
    }

    @Test fun `IOException se traduce a sin conexion`() = runTest {
        val r = llamadaSegura<Int> { throw IOException("timeout") }
        assertTrue(r is Resultado.Error && r.mensaje.startsWith("Sin conexión"))
    }

    @Test fun `HttpException usa el mensaje del servidor`() = runTest {
        val cuerpo = """{"error":"Ya existe"}""".toResponseBody("application/json".toMediaType())
        val r = llamadaSegura<Int> { throw HttpException(Response.error<Any>(409, cuerpo)) }
        assertEquals(Resultado.Error("Ya existe"), r)
    }

    @Test fun `HttpException sin cuerpo usa el codigo`() = runTest {
        val cuerpo = "".toResponseBody("application/json".toMediaType())
        val r = llamadaSegura<Int> { throw HttpException(Response.error<Any>(500, cuerpo)) }
        assertEquals(Resultado.Error("Error del servidor (500)"), r)
    }
}
