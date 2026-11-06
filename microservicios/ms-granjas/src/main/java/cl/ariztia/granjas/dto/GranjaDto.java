package cl.ariztia.granjas.dto;

import jakarta.validation.constraints.NotBlank;

/** [DTO] Granja (sirve de entrada y de salida). */
public record GranjaDto(
        Long id,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String ubicacion,
        Double latitud,
        Double longitud) {
}
