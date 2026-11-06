package cl.ariztia.bebederos.viewmodel

import cl.ariztia.bebederos.MainDispatcherRule
import cl.ariztia.bebederos.data.model.Rol
import cl.ariztia.bebederos.data.model.Usuario
import cl.ariztia.bebederos.data.repository.UsuariosRepository
import cl.ariztia.bebederos.util.Resultado
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

/**
 * [PRUEBA UNITARIA] SesionViewModel.
 * Se MOCKEA el repositorio (no hay red real); se prueba la lógica del ViewModel: validación y manejo de resultados.
 */
class SesionViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    private val repo = mockk<UsuariosRepository>()
    private val operario = Usuario(1, "Carlos Operario", "operario@ariztia.cl", Rol.OPERARIO)

    @Test fun `campos vacios muestran error y no llaman al servidor`() {
        val vm = SesionViewModel(repo)
        vm.iniciarSesion("", "")
        assertEquals("Ingresa tu correo y contraseña", vm.ui.value.error)
        coVerify(exactly = 0) { repo.login(any(), any()) }
    }

    @Test fun `login exitoso guarda el usuario`() {
        coEvery { repo.login("operario@ariztia.cl", "1234") } returns Resultado.Exito(operario)
        val vm = SesionViewModel(repo)
        vm.iniciarSesion(" operario@ariztia.cl ", "1234") // el correo se recorta (trim)
        assertEquals(operario, vm.usuario.value)
        assertNull(vm.ui.value.error)
    }

    @Test fun `login fallido muestra el mensaje del servidor`() {
        coEvery { repo.login(any(), any()) } returns Resultado.Error("Correo o contraseña incorrectos")
        val vm = SesionViewModel(repo)
        vm.iniciarSesion("a@b.cl", "mal")
        assertNull(vm.usuario.value)
        assertEquals("Correo o contraseña incorrectos", vm.ui.value.error)
    }

    @Test fun `cerrar sesion limpia el usuario`() {
        coEvery { repo.login(any(), any()) } returns Resultado.Exito(operario)
        val vm = SesionViewModel(repo)
        vm.iniciarSesion("a@b.cl", "1234")
        assertNotNull(vm.usuario.value)
        vm.cerrarSesion()
        assertNull(vm.usuario.value)
    }
}
