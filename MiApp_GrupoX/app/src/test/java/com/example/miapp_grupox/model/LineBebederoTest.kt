package com.example.miapp_grupox.model

import org.junit.Assert.assertEquals
import org.junit.Test

/** Regla de estado de una línea: < 27 Normal, 27 a 29,9 Advertencia, desde 30 Crítico. */
class LineBebederoTest {
    private fun linea(t: Double) = LineBebedero(granja = "G", galpon = "Ga", linea = "L", temperatura = t, fecha = "f")

    @Test fun `bajo 27 es Normal`() = assertEquals("Normal", linea(26.9).estado)
    @Test fun `27 es Advertencia`() = assertEquals("Advertencia", linea(27.0).estado)
    @Test fun `29 coma 9 es Advertencia`() = assertEquals("Advertencia", linea(29.9).estado)
    @Test fun `30 es Critico`() = assertEquals("Crítico", linea(30.0).estado)
}
