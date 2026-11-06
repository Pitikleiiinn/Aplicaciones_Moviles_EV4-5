package cl.ariztia.bebederos.util

import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** [PRUEBA UNITARIA] Manejo de errores de red: cómo se traducen las excepciones a mensajes. */
class ResultadoTest {

    @Test fun `extrae el mensaje de error del backend`() =
        assertEquals("Credenciales inválidas", extraerMensajeServidor("""{"error":"Credenciales inválidas"}"""))

    @Test fun `devuelve null si el cuerpo no trae error`() {
        assertNull(extraerMensajeServidor(null))
        assertNull(extraerMensajeServidor("""{"otro":"x"}"""))
    }

    @Test fun `llamada exitosa entrega Exito`() = runTest {
        val r = llamadaSegura { 42 }
        assertEquals(Resultado.Exito(42), r)
    }

    @Test fun `IOException se traduce a mensaje de sin conexion`() = runTest {
        val r = llamadaSegura<Int> { throw IOException("timeout") }
        assertTrue(r is Resultado.Error && r.mensaje.contains("Sin conexión"))
    }
}
