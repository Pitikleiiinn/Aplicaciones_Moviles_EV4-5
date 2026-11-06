package cl.ariztia.bebederos.viewmodel

import cl.ariztia.bebederos.MainDispatcherRule
import cl.ariztia.bebederos.data.model.EstadoLinea
import cl.ariztia.bebederos.data.model.Lectura
import cl.ariztia.bebederos.data.model.Linea
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.data.repository.TemperaturasRepository
import cl.ariztia.bebederos.util.Resultado
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/** [PRUEBA UNITARIA] LineasViewModel: combina datos de DOS microservicios (líneas + últimas lecturas). */
class LineasViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    private val granjas = mockk<GranjasRepository>()
    private val temperaturas = mockk<TemperaturasRepository>()
    private val a = Linea(1, "Línea A", 9, "Galpón 1", 1, "Los Aromos")
    private val b = Linea(2, "Línea B", 9, "Galpón 1", 1, "Los Aromos")

    @Test fun `une cada linea con su ultima lectura`() {
        coEvery { granjas.lineas(9) } returns Resultado.Exito(listOf(a, b))
        coEvery { temperaturas.ultimas(listOf(1, 2)) } returns
            Resultado.Exito(listOf(Lectura(7, 1, null, 31.0, EstadoLinea.CRITICO, "2026-11-04T10:00:00")))

        val vm = LineasViewModel(granjas, temperaturas, 9)
        vm.cargar()

        val datos = (vm.estado.value as UiState.Exito).datos
        assertEquals(31.0, datos[0].lectura?.temperatura)
        assertNull(datos[1].lectura) // la línea B no tiene lecturas
    }

    @Test fun `si ms-temperaturas falla igual muestra las lineas`() {
        coEvery { granjas.lineas(9) } returns Resultado.Exito(listOf(a))
        coEvery { temperaturas.ultimas(any()) } returns Resultado.Error("caído")
        val vm = LineasViewModel(granjas, temperaturas, 9)
        vm.cargar()
        val datos = (vm.estado.value as UiState.Exito).datos
        assertEquals(1, datos.size); assertNull(datos[0].lectura)
    }

    @Test fun `si ms-granjas falla el estado es Error`() {
        coEvery { granjas.lineas(9) } returns Resultado.Error("Sin conexión")
        val vm = LineasViewModel(granjas, temperaturas, 9)
        vm.cargar()
        assertTrue(vm.estado.value is UiState.Error)
    }
}
