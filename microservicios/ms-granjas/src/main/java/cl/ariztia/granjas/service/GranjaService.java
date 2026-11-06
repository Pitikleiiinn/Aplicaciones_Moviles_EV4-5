package cl.ariztia.granjas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.ariztia.granjas.dto.GranjaDto;
import cl.ariztia.granjas.exception.RecursoNoEncontradoException;
import cl.ariztia.granjas.mapper.GranjasMapper;
import cl.ariztia.granjas.model.Granja;
import cl.ariztia.granjas.repository.GranjaRepository;

/** [SERVICIO] CRUD de granjas. */
@Service
public class GranjaService {

    private final GranjaRepository repo;

    public GranjaService(GranjaRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<GranjaDto> listar() {
        return repo.findAll().stream().map(GranjasMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public GranjaDto obtener(Long id) {
        return GranjasMapper.toDto(buscar(id));
    }

    @Transactional
    public GranjaDto crear(GranjaDto dto) {
        Granja g = new Granja();
        aplicar(g, dto);
        return GranjasMapper.toDto(repo.save(g));
    }

    @Transactional
    public GranjaDto actualizar(Long id, GranjaDto dto) {
        Granja g = buscar(id);
        aplicar(g, dto);
        return GranjasMapper.toDto(repo.save(g));
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    /** Uso interno (también lo usa GalponService para validar que la granja exista) */
    public Granja buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Granja " + id + " no existe"));
    }

    private void aplicar(Granja g, GranjaDto dto) {
        g.setNombre(dto.nombre().trim());
        g.setUbicacion(dto.ubicacion());
        g.setLatitud(dto.latitud());
        g.setLongitud(dto.longitud());
    }
}
