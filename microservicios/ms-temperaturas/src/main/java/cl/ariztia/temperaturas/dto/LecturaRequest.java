package cl.ariztia.temperaturas.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/** [DTO ENTRADA] Registrar o corregir una lectura. El estado NO se envía: lo calcula el servidor. */
public record LecturaRequest(
        @NotNull(message = "lineaId es obligatorio") Long lineaId,
        String lineaNombre,
        @NotNull(message = "La temperatura es obligatoria")
        @DecimalMin(value = "-10.0", message = "Temperatura fuera de rango")
        @DecimalMax(value = "80.0", message = "Temperatura fuera de rango") Double temperatura) {
}
