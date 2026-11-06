package cl.ariztia.temperaturas.dto;

import java.time.LocalDateTime;

import cl.ariztia.temperaturas.model.EstadoLinea;

/** [DTO SALIDA] Lectura con su estado calculado. */
public record LecturaResponse(Long id, Long lineaId, String lineaNombre, Double temperatura,
                              EstadoLinea estado, LocalDateTime fechaHora) {
}
