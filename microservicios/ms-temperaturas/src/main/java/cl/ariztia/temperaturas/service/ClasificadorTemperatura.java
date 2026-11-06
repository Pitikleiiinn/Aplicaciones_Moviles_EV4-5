package cl.ariztia.temperaturas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cl.ariztia.temperaturas.model.EstadoLinea;

/**
 * [REGLA DE NEGOCIO] Decide el estado de una línea según su temperatura.
 * Los umbrales se leen de application.properties, así se pueden ajustar sin tocar el código.
 */
@Component
public class ClasificadorTemperatura {

    private final double limiteAdvertencia;
    private final double limiteCritico;

    public ClasificadorTemperatura(
            @Value("${ariztia.temperatura.advertencia:25}") double limiteAdvertencia,
            @Value("${ariztia.temperatura.critico:30}") double limiteCritico) {
        this.limiteAdvertencia = limiteAdvertencia;
        this.limiteCritico = limiteCritico;
    }

    public EstadoLinea clasificar(double temperatura) {
        if (temperatura > limiteCritico) return EstadoLinea.CRITICO;
        if (temperatura > limiteAdvertencia) return EstadoLinea.ADVERTENCIA;
        return EstadoLinea.NORMAL;
    }
}
