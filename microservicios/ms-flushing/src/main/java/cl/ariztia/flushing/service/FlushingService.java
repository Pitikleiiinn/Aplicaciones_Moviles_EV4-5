package cl.ariztia.flushing.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.ariztia.flushing.dto.FlushingRequest;
import cl.ariztia.flushing.dto.FlushingResponse;
import cl.ariztia.flushing.exception.RecursoNoEncontradoException;
import cl.ariztia.flushing.mapper.FlushingMapper;
import cl.ariztia.flushing.model.Flushing;
import cl.ariztia.flushing.repository.FlushingRepository;

/** [SERVICIO] CRUD completo de registros de flushing. */
@Service
public class FlushingService {

    private final FlushingRepository repo;

    public FlushingService(FlushingRepository repo) {
        this.repo = repo;
    }

    /** READ: todos, o solo los de una línea si se entrega lineaId. */
    @Transactional(readOnly = true)
    public List<FlushingResponse> listar(Long lineaId) {
        List<Flushing> datos = (lineaId == null)
                ? repo.findAllByOrderByFechaRegistroDescIdDesc()
                : repo.findByLineaIdOrderByFechaRegistroDescIdDesc(lineaId);
        return datos.stream().map(FlushingMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FlushingResponse obtener(Long id) {
        return FlushingMapper.toResponse(buscar(id));
    }

    /** CREATE */
    @Transactional
    public FlushingResponse crear(FlushingRequest req) {
        Flushing f = new Flushing();
        aplicar(f, req);
        return FlushingMapper.toResponse(repo.save(f));
    }

    /** UPDATE: corrige los datos (la fecha original de registro se conserva). */
    @Transactional
    public FlushingResponse actualizar(Long id, FlushingRequest req) {
        Flushing f = buscar(id);
        aplicar(f, req);
        return FlushingMapper.toResponse(repo.save(f));
    }

    /** DELETE */
    @Transactional
    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    private Flushing buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Flushing " + id + " no existe"));
    }

    private void aplicar(Flushing f, FlushingRequest req) {
        f.setLineaId(req.lineaId());
        f.setGranjaNombre(req.granjaNombre());
        f.setGalponNombre(req.galponNombre());
        f.setLineaNombre(req.lineaNombre());
        f.setResponsable(req.responsable().trim());
        f.setDuracionMinutos(req.duracionMinutos());
        f.setObservacion(req.observacion().trim());
    }
}
