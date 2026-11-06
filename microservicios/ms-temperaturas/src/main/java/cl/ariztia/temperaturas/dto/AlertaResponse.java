package cl.ariztia.temperaturas.dto;

import java.time.LocalDateTime;

import cl.ariztia.temperaturas.model.EstadoLinea;

/** [DTO SALIDA] Alerta para mostrar en la app. */
public record AlertaResponse(Long id, Long lineaId, String lineaNombre, Long lecturaId, String mensaje,
                             EstadoLinea estado, LocalDateTime fechaHora, boolean atendida) {
}
