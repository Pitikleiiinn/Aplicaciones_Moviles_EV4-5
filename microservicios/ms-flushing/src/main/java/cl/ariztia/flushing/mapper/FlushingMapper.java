package cl.ariztia.flushing.mapper;

import java.time.format.DateTimeFormatter;

import cl.ariztia.flushing.dto.FlushingResponse;
import cl.ariztia.flushing.model.Flushing;

/** [MAPPER] Entidad -> DTO. La fecha sale como "dd/MM/yyyy HH:mm", igual que en la app. */
public final class FlushingMapper {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private FlushingMapper() { }

    public static FlushingResponse toResponse(Flushing f) {
        return new FlushingResponse(f.getId(), f.getGranja(), f.getGalpon(), f.getLinea(), f.getResponsable(),
                f.getMotivo(), f.getObservaciones(), f.getFechaRegistro().format(FORMATO));
    }
}
