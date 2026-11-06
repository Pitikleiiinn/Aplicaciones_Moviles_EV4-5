package cl.ariztia.usuarios.dto;

import cl.ariztia.usuarios.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** [DTO ENTRADA] Crear / actualizar usuario. En PUT la contraseña es opcional (si va vacía se conserva la actual). */
public record UsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "Correo inválido") String email,
        String password,
        @NotNull(message = "El rol es obligatorio") Rol rol) {
}
