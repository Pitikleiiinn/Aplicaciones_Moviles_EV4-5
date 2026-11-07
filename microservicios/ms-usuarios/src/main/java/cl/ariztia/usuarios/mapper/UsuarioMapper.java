package cl.ariztia.usuarios.mapper;

import cl.ariztia.usuarios.dto.UsuarioResponse;
import cl.ariztia.usuarios.model.Usuario;

/** [MAPPER] Entidad -> DTO de salida. */
public final class UsuarioMapper {
    private UsuarioMapper() { }

    public static UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getUsuario(), u.getNombre(), u.getRol());
    }
}
