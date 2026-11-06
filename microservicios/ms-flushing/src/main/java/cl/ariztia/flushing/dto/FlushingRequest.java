package cl.ariztia.flushing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** [DTO ENTRADA] Datos del formulario "Registrar flushing". La fecha la pone el servidor. */
public record FlushingRequest(
        @NotNull(message = "lineaId es obligatorio") Long lineaId,
        @NotBlank(message = "La granja es obligatoria") String granjaNombre,
        @NotBlank(message = "El galpón es obligatorio") String galponNombre,
        @NotBlank(message = "La línea es obligatoria") String lineaNombre,
        @NotBlank(message = "El responsable es obligatorio") String responsable,
        @NotNull(message = "La duración es obligatoria") @Min(value = 1, message = "La duración mínima es 1 minuto") Integer duracionMinutos,
        @NotBlank(message = "La observación es obligatoria") @Size(max = 500, message = "Máximo 500 caracteres") String observacion) {
}
