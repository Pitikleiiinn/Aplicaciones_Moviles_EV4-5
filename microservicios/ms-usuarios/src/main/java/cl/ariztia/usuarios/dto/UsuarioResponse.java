package cl.ariztia.usuarios.dto;

import cl.ariztia.usuarios.model.Rol;

/** [DTO SALIDA] Lo que ve la app: jamás incluye la contraseña. */
public record UsuarioResponse(Long id, String nombre, String email, Rol rol) {
}
