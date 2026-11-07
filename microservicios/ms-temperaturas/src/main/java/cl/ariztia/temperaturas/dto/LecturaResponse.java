package cl.ariztia.temperaturas.dto;

/** [DTO SALIDA] Una medición del historial. */
public record LecturaResponse(Long id, Double temperatura, String estado, String fecha) {
}
