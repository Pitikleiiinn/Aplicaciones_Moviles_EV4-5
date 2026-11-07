package com.example.miapp_grupox.repository

import com.example.miapp_grupox.remote.ClimaActual
import com.example.miapp_grupox.remote.ClimaApi
import com.example.miapp_grupox.remote.ClimaResponse
import com.example.miapp_grupox.remote.LoginRequest
import com.example.miapp_grupox.remote.Resultado
import com.example.miapp_grupox.remote.UsuarioDto
import com.example.miapp_grupox.remote.UsuariosApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/** Login (ms-usuarios) y clima (API externa) con servicios simulados. */
class UsuariosYClimaRepositoryTest {

    // ----- Usuarios -----
    private val usuariosApi = mockk<UsuariosApi>()
    private val usuarios = UsuariosRepository(usuariosApi)

    @Test fun `login correcto devuelve el usuario con su rol`() = runTest {
        val dto = UsuarioDto(1, "operario", "Operario", "OPERARIO")
        coEvery { usuariosApi.login(LoginRequest("operario", "1234")) } returns dto
        assertEquals(Resultado.Exito(dto), usuarios.login("operario", "1234"))
    }

    @Test fun `login incorrecto devuelve el mensaje del servidor`() = runTest {
        val cuerpo = """{"error":"Usuario o contraseña incorrectos"}""".toResponseBody("application/json".toMediaType())
        coEvery { usuariosApi.login(any()) } throws HttpException(Response.error<Any>(401, cuerpo))
        assertEquals(Resultado.Error("Usuario o contraseña incorrectos"), usuarios.login("operario", "mal"))
    }

    // ----- Clima (API externa) -----
    private val climaApi = mockk<ClimaApi>()
    private val clima = ClimaRepository(climaApi)

    @Test fun `clima correcto entrega temperatura y humedad`() = runTest {
        coEvery { climaApi.climaActual(-33.69, -71.21, ClimaRepository.CAMPOS) } returns ClimaResponse(ClimaActual(18.3, 60))
        assertEquals(Resultado.Exito(ClimaActual(18.3, 60)), clima.actual(-33.69, -71.21))
    }

    @Test fun `respuesta sin datos actuales es error`() = runTest {
        coEvery { climaApi.climaActual(any(), any(), any()) } returns ClimaResponse(null)
        assertEquals(Resultado.Error("Clima no disponible"), clima.actual(0.0, 0.0))
    }

    @Test fun `clima sin conexion es error`() = runTest {
        coEvery { climaApi.climaActual(any(), any(), any()) } throws java.io.IOException("sin red")
        assertEquals(true, clima.actual(0.0, 0.0) is Resultado.Error)
    }
}
