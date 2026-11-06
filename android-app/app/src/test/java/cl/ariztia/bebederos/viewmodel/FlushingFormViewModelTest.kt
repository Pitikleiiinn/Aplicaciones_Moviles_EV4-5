package cl.ariztia.bebederos.viewmodel

import cl.ariztia.bebederos.MainDispatcherRule
import cl.ariztia.bebederos.data.model.Flushing
import cl.ariztia.bebederos.data.model.FlushingRequest
import cl.ariztia.bebederos.data.model.Galpon
import cl.ariztia.bebederos.data.model.Granja
import cl.ariztia.bebederos.data.model.Linea
import cl.ariztia.bebederos.data.repository.FlushingRepository
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.util.Resultado
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * [PRUEBA UNITARIA] FlushingFormViewModel: validación del formulario, cascada de menús y envío.
 * Se mockean los dos repositorios (ms-granjas y ms-flushing).
 */
class FlushingFormViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    private val granjasRepo = mockk<GranjasRepository>()
    private val flushingRepo = mockk<FlushingRepository>()

    private val granja = Granja(1, "Los Aromos", "Melipilla", -33.6, -71.2)
    private val galpon = Galpon(1, "Galpón 1", 1)
    private val linea = Linea(1, "Línea A", 1, "Galpón 1", 1, "Los Aromos")

    private fun vmConCascada(): FlushingFormViewModel {
        coEvery { granjasRepo.galpones(1) } returns Resultado.Exito(listOf(galpon))
        coEvery { granjasRepo.lineas(1) } returns Resultado.Exito(listOf(linea))
        return FlushingFormViewModel(granjasRepo, flushingRepo, "Carlos").also {
            it.seleccionarGranja(granja); it.seleccionarGalpon(galpon); it.seleccionarLinea(linea)
        }
    }

    @Test fun `formulario vacio devuelve error en todos los campos`() {
        val vm = FlushingFormViewModel(granjasRepo, flushingRepo, "")
        val errores = vm.validar()
        assertEquals(setOf("granja", "galpon", "linea", "responsable", "duracion", "observacion"), errores.keys)
    }

    @Test fun `guardar con errores no llama al servidor y avisa al usuario`() {
        val vm = FlushingFormViewModel(granjasRepo, flushingRepo, "Carlos")
        vm.guardar()
        assertTrue(vm.ui.value.errores.isNotEmpty())
        assertEquals("Completa todos los campos para registrar el flushing", vm.ui.value.mensaje)
        coVerify(exactly = 0) { flushingRepo.crear(any()) }
    }

    @Test fun `la cascada carga galpones y lineas al elegir`() {
        val vm = vmConCascada()
        assertEquals(listOf(galpon), vm.ui.value.galpones)
        assertEquals(listOf(linea), vm.ui.value.lineas)
    }

    @Test fun `duracion solo acepta digitos`() {
        val vm = FlushingFormViewModel(granjasRepo, flushingRepo, "Carlos")
        vm.cambiarDuracion("1a5b")
        assertEquals("15", vm.ui.value.duracion)
    }

    @Test fun `guardar valido envia el request correcto y limpia los campos`() {
        val capturado = slot<FlushingRequest>()
        coEvery { flushingRepo.crear(capture(capturado)) } returns Resultado.Exito(
            Flushing(10, 1, "Los Aromos", "Galpón 1", "Línea A", "Carlos", 15, "ok", "2026-11-04T10:00:00"))
        val vm = vmConCascada()
        vm.cambiarDuracion("15")
        vm.cambiarObservacion("  Limpieza completa  ")
        vm.guardar()

        assertEquals(FlushingRequest(1, "Los Aromos", "Galpón 1", "Línea A", "Carlos", 15, "Limpieza completa"), capturado.captured)
        assertEquals("Flushing registrado correctamente", vm.ui.value.mensaje)
        assertEquals("", vm.ui.value.observacion)
    }

    @Test fun `si el servidor falla se muestra su mensaje`() {
        coEvery { flushingRepo.crear(any()) } returns Resultado.Error("Sin conexión con el servidor")
        val vm = vmConCascada()
        vm.cambiarDuracion("10"); vm.cambiarObservacion("x")
        vm.guardar()
        assertEquals("Sin conexión con el servidor", vm.ui.value.mensaje)
    }
}
