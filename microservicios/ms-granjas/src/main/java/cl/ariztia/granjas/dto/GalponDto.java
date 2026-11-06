package cl.ariztia.granjas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** [DTO] Galpón. Se referencia a su granja solo por id (evita JSON recursivo). */
public record GalponDto(
        Long id,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotNull(message = "granjaId es obligatorio") Long granjaId) {
}
