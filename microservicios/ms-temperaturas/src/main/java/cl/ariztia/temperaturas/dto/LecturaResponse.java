package cl.ariztia.temperaturas.dto;

/** DTO SALID Una medición del historial. */
public record LecturaResponse(Long id, Double temperatura, String estado, String fecha) {
}
