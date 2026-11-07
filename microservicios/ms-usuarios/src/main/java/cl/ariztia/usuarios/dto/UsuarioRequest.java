package cl.ariztia.usuarios.dto;

import cl.ariztia.usuarios.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** [DTO ENTRADA] Crear / actualizar usuario. En PUT la clave es opcional (vacía = se conserva la actual). */
public record UsuarioRequest(
        @NotBlank(message = "El usuario es obligatorio") @Size(min = 4, message = "El usuario debe tener al menos 4 caracteres") String usuario,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String clave,
        @NotNull(message = "El rol es obligatorio") Rol rol) {
}
