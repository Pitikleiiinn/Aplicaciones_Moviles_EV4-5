package cl.ariztia.temperaturas.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cl.ariztia.temperaturas.dto.LecturaResponse;
import cl.ariztia.temperaturas.dto.LineaResponse;
import cl.ariztia.temperaturas.model.EstadoLinea;
import cl.ariztia.temperaturas.model.Lectura;
import cl.ariztia.temperaturas.model.LineaBebedero;

/** [MAPPER] Entidad -> DTO. Las fechas salen como texto "dd/MM/yyyy HH:mm", igual que en la app. */
public final class TemperaturasMapper {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private TemperaturasMapper() { }

    public static String formatear(LocalDateTime fecha) {
        return fecha.format(FORMATO);
    }

    public static LineaResponse toResponse(LineaBebedero l, EstadoLinea estado) {
        return new LineaResponse(l.getId(), l.getGranja(), l.getGalpon(), l.getLinea(), l.getTemperatura(),
                estado.getEtiqueta(), formatear(l.getFechaActualizacion()));
    }

    public static LecturaResponse toResponse(Lectura l) {
        return new LecturaResponse(l.getId(), l.getTemperatura(), l.getEstado().getEtiqueta(), formatear(l.getFechaHora()));
    }
}
