package cl.ariztia.granjas.mapper;

import cl.ariztia.granjas.dto.GalponDto;
import cl.ariztia.granjas.dto.GranjaDto;
import cl.ariztia.granjas.dto.LineaDto;
import cl.ariztia.granjas.model.Galpon;
import cl.ariztia.granjas.model.Granja;
import cl.ariztia.granjas.model.LineaBebedero;

/** [MAPPER] Entidad -> DTO para los tres niveles. */
public final class GranjasMapper {
    private GranjasMapper() { }

    public static GranjaDto toDto(Granja g) {
        return new GranjaDto(g.getId(), g.getNombre(), g.getUbicacion(), g.getLatitud(), g.getLongitud());
    }

    public static GalponDto toDto(Galpon g) {
        return new GalponDto(g.getId(), g.getNombre(), g.getGranja().getId());
    }

    public static LineaDto toDto(LineaBebedero l) {
        Galpon galpon = l.getGalpon();
        Granja granja = galpon.getGranja();
        return new LineaDto(l.getId(), l.getNombre(), galpon.getId(), galpon.getNombre(), granja.getId(), granja.getNombre());
    }
}
