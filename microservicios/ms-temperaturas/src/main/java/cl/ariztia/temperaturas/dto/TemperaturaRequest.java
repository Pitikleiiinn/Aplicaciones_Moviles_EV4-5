package cl.ariztia.temperaturas.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** [DTO ENTRADA] Registrar temperatura de una línea. El estado NO se envía: lo calcula el servidor. */
public record TemperaturaRequest(
        @NotBlank(message = "La granja es obligatoria") String granja,
        @NotBlank(message = "El galpón es obligatorio") String galpon,
        @NotBlank(message = "La línea es obligatoria") String linea,
        @NotNull(message = "La temperatura es obligatoria")
        @DecimalMin(value = "-10.0", message = "Temperatura fuera de rango")
        @DecimalMax(value = "80.0", message = "Temperatura fuera de rango") Double temperatura) {
}
