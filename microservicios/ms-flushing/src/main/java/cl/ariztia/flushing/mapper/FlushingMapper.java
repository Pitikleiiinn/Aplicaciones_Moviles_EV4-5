package cl.ariztia.flushing.mapper;

import cl.ariztia.flushing.dto.FlushingResponse;
import cl.ariztia.flushing.model.Flushing;

/** [MAPPER] Entidad -> DTO de salida. */
public final class FlushingMapper {
    private FlushingMapper() { }

    public static FlushingResponse toResponse(Flushing f) {
        return new FlushingResponse(f.getId(), f.getLineaId(), f.getGranjaNombre(), f.getGalponNombre(), f.getLineaNombre(),
                f.getResponsable(), f.getDuracionMinutos(), f.getObservacion(), f.getFechaRegistro());
    }
}
