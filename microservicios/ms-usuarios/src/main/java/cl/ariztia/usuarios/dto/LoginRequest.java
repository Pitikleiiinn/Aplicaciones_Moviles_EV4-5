package cl.ariztia.usuarios.dto;

import jakarta.validation.constraints.NotBlank;

/** [DTO ENTRADA] Cuerpo del POST /api/usuarios/login (mismos campos que el formulario de la app). */
public record LoginRequest(
        @NotBlank(message = "Ingresa tu usuario") String usuario,
        @NotBlank(message = "Ingresa tu contraseña") String clave) {
}
