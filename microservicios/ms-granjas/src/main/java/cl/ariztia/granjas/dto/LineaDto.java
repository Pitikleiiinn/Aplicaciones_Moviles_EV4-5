package cl.ariztia.granjas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * [DTO] Línea de bebederos. En la respuesta incluye los nombres de galpón y granja
 * para que la app arme la descripción "Granja > Galpón > Línea" sin hacer más llamadas.
 * Al crear/editar solo se necesitan "nombre" y "galponId".
 */
public record LineaDto(
        Long id,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotNull(message = "galponId es obligatorio") Long galponId,
        String galponNombre,
        Long granjaId,
        String granjaNombre) {
}
