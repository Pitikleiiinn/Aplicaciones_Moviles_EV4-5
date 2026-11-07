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

    @Transactional(readOnly = true)
    public List<FlushingResponse> listar() {
        return repo.findAllByOrderByIdDesc().stream().map(FlushingMapper::toResponse).toList();
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

    /** UPDATE: se conserva la fecha original del registro. */
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
        f.setGranja(req.granja());
        f.setGalpon(req.galpon());
        f.setLinea(req.linea());
        f.setResponsable(req.responsable().trim());
        f.setMotivo(req.motivo());
        f.setObservaciones(req.observaciones().trim());
    }
}
