package cl.ariztia.temperaturas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import cl.ariztia.temperaturas.model.EstadoLinea;

/** [PRUEBA UNITARIA] Verifica la regla de negocio de umbrales (25 °C advertencia, 30 °C crítico). */
class ClasificadorTemperaturaTest {

    private final ClasificadorTemperatura clasificador = new ClasificadorTemperatura(25, 30);

    @Test
    void temperaturaBajaEsNormal() {
        assertEquals(EstadoLinea.NORMAL, clasificador.clasificar(20.0));
    }

    @Test
    void enElLimiteDeAdvertenciaSigueNormal() {
        assertEquals(EstadoLinea.NORMAL, clasificador.clasificar(25.0));
    }

    @Test
    void sobreElLimiteEsAdvertencia() {
        assertEquals(EstadoLinea.ADVERTENCIA, clasificador.clasificar(25.1));
    }

    @Test
    void sobreElLimiteCriticoEsCritico() {
        assertEquals(EstadoLinea.CRITICO, clasificador.clasificar(30.5));
    }
}
