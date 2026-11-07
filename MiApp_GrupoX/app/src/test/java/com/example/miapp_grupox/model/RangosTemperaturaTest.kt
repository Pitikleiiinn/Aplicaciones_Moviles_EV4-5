package com.example.miapp_grupox.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Rangos del combo box y datos de formulario. */
class RangosTemperaturaTest {
    @Test fun `cada rango tiene un valor representativo`() {
        assertEquals(18.0, RangosTemperatura.valorDe("Menos de 20 °C")!!, 0.0)
        assertEquals(32.0, RangosTemperatura.valorDe("30 °C o más")!!, 0.0)
    }

    @Test fun `rango desconocido devuelve null`() = assertNull(RangosTemperatura.valorDe("otro"))

    @Test fun `etiquetaDe ubica la temperatura en su rango`() {
        assertEquals("Menos de 20 °C", RangosTemperatura.etiquetaDe(19.9))
        assertEquals("20 °C a 26,9 °C", RangosTemperatura.etiquetaDe(26.9))
        assertEquals("27 °C a 29,9 °C", RangosTemperatura.etiquetaDe(27.0))
        assertEquals("30 °C o más", RangosTemperatura.etiquetaDe(30.0))
    }

    @Test fun `el valor representativo cae en el mismo estado que su rango`() {
        RangosTemperatura.lista.forEach {
            assertEquals(it.etiqueta, RangosTemperatura.etiquetaDe(it.valorRepresentativo))
        }
    }

    @Test fun `todas las granjas tienen coordenadas para el clima`() {
        assertTrue(OpcionesBebedero.granjas.all { OpcionesBebedero.coordenadas.containsKey(it) })
    }
}
