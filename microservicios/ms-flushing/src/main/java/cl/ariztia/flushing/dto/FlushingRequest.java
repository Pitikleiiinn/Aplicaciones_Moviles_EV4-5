package cl.ariztia.flushing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * [DTO ENTRADA] Crear / corregir un flushing. Validaciones iguales a ValidadorFlushing de la app:
 * responsable solo letras (mín. 3), motivo obligatorio, observaciones de 5 a 500 caracteres.
 * La fecha NO se envía: la asigna el servidor.
 */
public record FlushingRequest(
        @NotBlank(message = "La granja es obligatoria") String granja,
        @NotBlank(message = "El galpón es obligatorio") String galpon,
        @NotBlank(message = "La línea es obligatoria") String linea,
        @NotBlank(message = "Ingresa el nombre del responsable")
        @Size(min = 3, message = "El nombre debe tener al menos 3 letras")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+$", message = "El nombre solo puede tener letras") String responsable,
        @NotBlank(message = "Selecciona el motivo del flushing") String motivo,
        @NotBlank(message = "Escribe una observación")
        @Size(min = 5, max = 500, message = "La observación debe tener entre 5 y 500 caracteres") String observaciones) {
}
