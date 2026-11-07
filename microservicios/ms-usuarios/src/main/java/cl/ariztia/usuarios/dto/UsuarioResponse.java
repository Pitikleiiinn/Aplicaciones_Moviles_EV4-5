package cl.ariztia.usuarios.dto;

import cl.ariztia.usuarios.model.Rol;

/** [DTO SALIDA] Lo que ve la app: jamás incluye la clave. */
public record UsuarioResponse(Long id, String usuario, String nombre, Rol rol) {
}
