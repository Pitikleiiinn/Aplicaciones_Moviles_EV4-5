package cl.ariztia.flushing.dto;

/** [DTO SALIDA] Mismos nombres de campo que FlusingEntity de la app (id, granja, ..., fecha). */
public record FlushingResponse(Long id, String granja, String galpon, String linea, String responsable,
                               String motivo, String observaciones, String fecha) {
}
