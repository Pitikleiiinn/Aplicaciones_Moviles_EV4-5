package com.example.miapp_grupox.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Validaciones de los formularios (lógica separada de la interfaz). */
class ValidadoresTest {

    // ----- Temperatura -----
    @Test fun `temperatura sin rango es invalida`() {
        val r = ValidadorTemperatura.validar("")
        assertFalse(r.valido)
        assertEquals("Selecciona un rango de temperatura", r.errores["rango"])
    }

    @Test fun `temperatura con rango valido es valida`() =
        assertTrue(ValidadorTemperatura.validar("30 °C o más").valido)

    // ----- Flushing -----
    @Test fun `flushing valido no tiene errores`() =
        assertTrue(ValidadorFlushing.validar("Carlos Soto", "Mantenimiento", "Todo bien", true).valido)

    @Test fun `flushing vacio reporta todos los campos`() {
        val r = ValidadorFlushing.validar("", "", "", false)
        assertEquals(setOf("responsable", "motivo", "observaciones", "foto"), r.errores.keys)
    }

    @Test fun `responsable con numeros es invalido`() =
        assertEquals("El nombre solo puede tener letras", ValidadorFlushing.validar("Carlos1", "x", "observacion", true).errores["responsable"])

    @Test fun `responsable muy corto es invalido`() =
        assertEquals("El nombre debe tener al menos 3 letras", ValidadorFlushing.validar("Al", "x", "observacion", true).errores["responsable"])

    @Test fun `observacion corta es invalida`() =
        assertEquals("La observación debe tener al menos 5 caracteres", ValidadorFlushing.validarObservacion("hola"))

    @Test fun `observacion valida devuelve null`() = assertNull(ValidadorFlushing.validarObservacion("Limpieza completa"))

    // ----- Login (campos) -----
    @Test fun `login con campos vacios marca ambos errores`() {
        val r = LoginValidator.validarCampos("", "")
        assertFalse(r.exitoso)
        assertEquals("Ingresa tu usuario", r.errorUsuario)
        assertEquals("Ingresa tu contraseña", r.errorClave)
        assertEquals("Revisa los campos marcados", r.mensaje)
    }

    @Test fun `login con campos cortos es invalido`() {
        val r = LoginValidator.validarCampos("abc", "12")
        assertEquals("El usuario debe tener al menos 4 caracteres", r.errorUsuario)
        assertEquals("La contraseña debe tener al menos 4 caracteres", r.errorClave)
    }

    @Test fun `login con campos correctos pasa la validacion de formato`() {
        val r = LoginValidator.validarCampos("operario", "1234")
        assertTrue(r.exitoso)
        assertNull(r.mensaje)
    }

    @Test fun `verificacion local del Parcial 2 sigue funcionando`() {
        assertTrue(LoginValidator.validar("operario", "1234").exitoso)
        assertEquals("Usuario o contraseña incorrectos", LoginValidator.validar("operario", "9999").mensaje)
    }
}
