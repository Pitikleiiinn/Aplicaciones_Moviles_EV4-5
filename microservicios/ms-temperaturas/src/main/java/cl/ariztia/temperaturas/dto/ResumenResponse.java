package cl.ariztia.temperaturas.dto;

/** [DTO SALIDA] Métricas del panel del supervisor. */
public record ResumenResponse(int totalLineas, int normales, int advertencias, int criticas) {
}
