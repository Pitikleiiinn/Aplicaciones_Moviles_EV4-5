package cl.ariztia.flushing.dto;

import java.time.LocalDateTime;

/** [DTO SALIDA] Flushing registrado. */
public record FlushingResponse(Long id, Long lineaId, String granjaNombre, String galponNombre, String lineaNombre,
                               String responsable, Integer duracionMinutos, String observacion,
                               LocalDateTime fechaRegistro) {
}
