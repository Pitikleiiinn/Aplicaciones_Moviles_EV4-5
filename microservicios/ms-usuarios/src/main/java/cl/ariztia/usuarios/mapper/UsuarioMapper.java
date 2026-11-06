package cl.ariztia.usuarios.mapper;

import cl.ariztia.usuarios.dto.UsuarioResponse;
import cl.ariztia.usuarios.model.Usuario;

/** [MAPPER] Convierte entidad -> DTO de salida. */
public final class UsuarioMapper {
    private UsuarioMapper() { }

    public static UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol());
    }
}
