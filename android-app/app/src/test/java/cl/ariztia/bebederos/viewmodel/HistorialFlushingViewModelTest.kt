package cl.ariztia.bebederos.viewmodel

import cl.ariztia.bebederos.MainDispatcherRule
import cl.ariztia.bebederos.data.model.Flushing
import cl.ariztia.bebederos.data.model.FlushingRequest
import cl.ariztia.bebederos.data.repository.FlushingRepository
import cl.ariztia.bebederos.util.Resultado
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/** [PRUEBA UNITARIA] HistorialFlushingViewModel: leer, corregir (UPDATE) y eliminar (DELETE). */
class HistorialFlushingViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    private val repo = mockk<FlushingRepository>()
    private val f = Flushing(3, 1, "Los Aromos", "Galpón 1", "Línea A", "Carlos", 15, "orig", "2026-11-04T10:00:00")

    @Test fun `eliminar llama al repositorio y recarga la lista`() {
        coEvery { repo.eliminar(3) } returns Resultado.Exito(Unit)
        coEvery { repo.listar() } returns Resultado.Exito(emptyList())
        val vm = HistorialFlushingViewModel(repo)
        vm.eliminar(3)
        coVerify { repo.eliminar(3) }
        assertEquals("Flushing eliminado", vm.mensaje.value)
    }

    @Test fun `corregir con datos invalidos no llama al servidor`() {
        val vm = HistorialFlushingViewModel(repo)
        vm.corregir(f, 0, "texto")
        assertEquals("Duración (mínimo 1) y observación son obligatorias", vm.mensaje.value)
        coVerify(exactly = 0) { repo.actualizar(any(), any()) }
    }

    @Test fun `corregir envia los datos nuevos conservando el resto`() {
        coEvery { repo.actualizar(3, any()) } returns Resultado.Exito(f)
        coEvery { repo.listar() } returns Resultado.Exito(listOf(f))
        val vm = HistorialFlushingViewModel(repo)
        vm.corregir(f, 25, " nueva ")
        coVerify { repo.actualizar(3, FlushingRequest(1, "Los Aromos", "Galpón 1", "Línea A", "Carlos", 25, "nueva")) }
        assertEquals("Flushing actualizado", vm.mensaje.value)
    }
}
