package cl.ariztia.granjas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.ariztia.granjas.dto.GalponDto;
import cl.ariztia.granjas.exception.RecursoNoEncontradoException;
import cl.ariztia.granjas.mapper.GranjasMapper;
import cl.ariztia.granjas.model.Galpon;
import cl.ariztia.granjas.repository.GalponRepository;

/** [SERVICIO] CRUD de galpones. */
@Service
public class GalponService {

    private final GalponRepository repo;
    private final GranjaService granjaService;

    public GalponService(GalponRepository repo, GranjaService granjaService) {
        this.repo = repo;
        this.granjaService = granjaService;
    }

    @Transactional(readOnly = true)
    public List<GalponDto> listarPorGranja(Long granjaId) {
        granjaService.buscar(granjaId); // lanza 404 si la granja no existe
        return repo.findByGranjaIdOrderByNombre(granjaId).stream().map(GranjasMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public GalponDto obtener(Long id) {
        return GranjasMapper.toDto(buscar(id));
    }

    @Transactional
    public GalponDto crear(GalponDto dto) {
        Galpon g = new Galpon();
        g.setNombre(dto.nombre().trim());
        g.setGranja(granjaService.buscar(dto.granjaId()));
        return GranjasMapper.toDto(repo.save(g));
    }

    @Transactional
    public GalponDto actualizar(Long id, GalponDto dto) {
        Galpon g = buscar(id);
        g.setNombre(dto.nombre().trim());
        g.setGranja(granjaService.buscar(dto.granjaId()));
        return GranjasMapper.toDto(repo.save(g));
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    public Galpon buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Galpón " + id + " no existe"));
    }
}
