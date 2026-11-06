package cl.ariztia.bebederos.viewmodel

import cl.ariztia.bebederos.MainDispatcherRule
import cl.ariztia.bebederos.data.model.EstadoLinea
import cl.ariztia.bebederos.data.model.Lectura
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.data.repository.TemperaturasRepository
import cl.ariztia.bebederos.util.Resultado
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

/** [PRUEBA UNITARIA] RegistrarTemperaturaViewModel: validación del número y envío a ms-temperaturas. */
class RegistrarTemperaturaViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    private val granjas = mockk<GranjasRepository>(relaxed = true)
    private val temperaturas = mockk<TemperaturasRepository>()
    private fun vm() = RegistrarTemperaturaViewModel(granjas, temperaturas, lineaId = 5)

    @Test fun `texto no numerico es invalido`() = assertNotNull(vm().validar("abc"))
    @Test fun `fuera de rango es invalido`() { assertNotNull(vm().validar("120")); assertNotNull(vm().validar("-50")) }
    @Test fun `acepta coma decimal`() = assertNull(vm().validar("24,5"))

    @Test fun `guardar invalido no llama al servidor`() {
        val vm = vm(); vm.cambiarTexto("xx"); vm.guardar()
        assertNotNull(vm.ui.value.error)
        coVerify(exactly = 0) { temperaturas.registrar(any(), any(), any()) }
    }

    @Test fun `guardar valido convierte la coma y marca exito`() {
        coEvery { temperaturas.registrar(5, any(), 31.5) } returns
            Resultado.Exito(Lectura(1, 5, null, 31.5, EstadoLinea.CRITICO, "2026-11-04T10:00:00"))
        val vm = vm(); vm.cambiarTexto("31,5"); vm.guardar()
        assertEquals("Registrado: 31.5 °C (CRITICO)", vm.ui.value.exito)
    }
}
