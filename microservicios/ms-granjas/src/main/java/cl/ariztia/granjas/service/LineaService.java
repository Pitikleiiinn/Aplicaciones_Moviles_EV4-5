package cl.ariztia.granjas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.ariztia.granjas.dto.LineaDto;
import cl.ariztia.granjas.exception.RecursoNoEncontradoException;
import cl.ariztia.granjas.mapper.GranjasMapper;
import cl.ariztia.granjas.model.LineaBebedero;
import cl.ariztia.granjas.repository.LineaBebederoRepository;

/** [SERVICIO] CRUD de líneas de bebederos. */
@Service
public class LineaService {

    private final LineaBebederoRepository repo;
    private final GalponService galponService;

    public LineaService(LineaBebederoRepository repo, GalponService galponService) {
        this.repo = repo;
        this.galponService = galponService;
    }

    @Transactional(readOnly = true)
    public List<LineaDto> listarPorGalpon(Long galponId) {
        galponService.buscar(galponId); // 404 si el galpón no existe
        return repo.findByGalponIdOrderByNombre(galponId).stream().map(GranjasMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public LineaDto obtener(Long id) {
        return GranjasMapper.toDto(buscar(id));
    }

    @Transactional
    public LineaDto crear(LineaDto dto) {
        LineaBebedero l = new LineaBebedero();
        l.setNombre(dto.nombre().trim());
        l.setGalpon(galponService.buscar(dto.galponId()));
        return GranjasMapper.toDto(repo.save(l));
    }

    @Transactional
    public LineaDto actualizar(Long id, LineaDto dto) {
        LineaBebedero l = buscar(id);
        l.setNombre(dto.nombre().trim());
        l.setGalpon(galponService.buscar(dto.galponId()));
        return GranjasMapper.toDto(repo.save(l));
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    private LineaBebedero buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Línea " + id + " no existe"));
    }
}
