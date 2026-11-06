package cl.ariztia.temperaturas.mapper;

import cl.ariztia.temperaturas.dto.AlertaResponse;
import cl.ariztia.temperaturas.dto.LecturaResponse;
import cl.ariztia.temperaturas.model.Alerta;
import cl.ariztia.temperaturas.model.Lectura;

/** [MAPPER] Entidad -> DTO de salida. */
public final class TemperaturasMapper {
    private TemperaturasMapper() { }

    public static LecturaResponse toResponse(Lectura l) {
        return new LecturaResponse(l.getId(), l.getLineaId(), l.getLineaNombre(), l.getTemperatura(), l.getEstado(), l.getFechaHora());
    }

    public static AlertaResponse toResponse(Alerta a) {
        return new AlertaResponse(a.getId(), a.getLineaId(), a.getLineaNombre(), a.getLecturaId(), a.getMensaje(),
                a.getEstado(), a.getFechaHora(), a.isAtendida());
    }
}
