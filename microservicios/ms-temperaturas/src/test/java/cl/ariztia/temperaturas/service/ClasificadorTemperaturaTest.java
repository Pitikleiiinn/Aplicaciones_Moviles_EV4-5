package cl.ariztia.temperaturas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import cl.ariztia.temperaturas.model.EstadoLinea;

/** [PRUEBA UNITARIA] Regla de estados: < 27 Normal, 27 a 29,9 Advertencia, >= 30 Crítico. */
class ClasificadorTemperaturaTest {

    private final ClasificadorTemperatura clasificador = new ClasificadorTemperatura(27, 30);

    @Test void bajoElLimiteEsNormal() { assertEquals(EstadoLinea.NORMAL, clasificador.clasificar(26.9)); }
    @Test void exactamente27EsAdvertencia() { assertEquals(EstadoLinea.ADVERTENCIA, clasificador.clasificar(27.0)); }
    @Test void justoBajo30EsAdvertencia() { assertEquals(EstadoLinea.ADVERTENCIA, clasificador.clasificar(29.9)); }
    @Test void exactamente30EsCritico() { assertEquals(EstadoLinea.CRITICO, clasificador.clasificar(30.0)); }
}
