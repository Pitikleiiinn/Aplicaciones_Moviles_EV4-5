package cl.ariztia.usuarios.dto;

import jakarta.validation.constraints.NotBlank;

/** [DTO ENTRADA] Cuerpo del POST /api/usuarios/login */
public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio") String email,
        @NotBlank(message = "La contraseña es obligatoria") String password) {
}
