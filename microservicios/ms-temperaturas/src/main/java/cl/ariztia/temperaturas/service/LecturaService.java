package cl.ariztia.temperaturas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.ariztia.temperaturas.dto.LecturaRequest;
import cl.ariztia.temperaturas.dto.LecturaResponse;
import cl.ariztia.temperaturas.exception.RecursoNoEncontradoException;
import cl.ariztia.temperaturas.mapper.TemperaturasMapper;
import cl.ariztia.temperaturas.model.Lectura;
import cl.ariztia.temperaturas.repository.AlertaRepository;
import cl.ariztia.temperaturas.repository.LecturaRepository;

/** [SERVICIO] Registro, consulta, corrección y borrado de lecturas de temperatura. */
@Service
public class LecturaService {

    private final LecturaRepository repo;
    private final AlertaRepository alertaRepo;
    private final ClasificadorTemperatura clasificador;
    private final AlertaService alertaService;

    public LecturaService(LecturaRepository repo, AlertaRepository alertaRepo,
                          ClasificadorTemperatura clasificador, AlertaService alertaService) {
        this.repo = repo;
        this.alertaRepo = alertaRepo;
        this.clasificador = clasificador;
        this.alertaService = alertaService;
    }

    /** CREATE: guarda la lectura, calcula su estado y dispara una alerta si corresponde. */
    @Transactional
    public LecturaResponse registrar(LecturaRequest req) {
        Lectura l = new Lectura();
        l.setLineaId(req.lineaId());
        l.setLineaNombre(req.lineaNombre());
        l.setTemperatura(req.temperatura());
        l.setEstado(clasificador.clasificar(req.temperatura()));
        l.setFechaHora(LocalDateTime.now());
        Lectura guardada = repo.save(l);
        alertaService.generarSiCorresponde(guardada);
        return TemperaturasMapper.toResponse(guardada);
    }

    /** READ: historial de una línea (más reciente primero). */
    @Transactional(readOnly = true)
    public List<LecturaResponse> historial(Long lineaId) {
        return repo.findByLineaIdOrderByFechaHoraDescIdDesc(lineaId).stream().map(TemperaturasMapper::toResponse).toList();
    }

    /** READ: última lectura de una línea (404 si aún no tiene). */
    @Transactional(readOnly = true)
    public LecturaResponse ultima(Long lineaId) {
        return repo.findFirstByLineaIdOrderByFechaHoraDescIdDesc(lineaId)
                .map(TemperaturasMapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("La línea " + lineaId + " no tiene lecturas"));
    }

    /** READ: última lectura de VARIAS líneas en una sola llamada (para listas). Las que no tienen lectura se omiten. */
    @Transactional(readOnly = true)
    public List<LecturaResponse> ultimas(List<Long> lineaIds) {
        return lineaIds.stream()
                .map(repo::findFirstByLineaIdOrderByFechaHoraDescIdDesc)
                .flatMap(java.util.Optional::stream)
                .map(TemperaturasMapper::toResponse)
                .toList();
    }

    /** UPDATE: corrige la temperatura y recalcula el estado. */
    @Transactional
    public LecturaResponse actualizar(Long id, LecturaRequest req) {
        Lectura l = buscar(id);
        l.setTemperatura(req.temperatura());
        l.setEstado(clasificador.clasificar(req.temperatura()));
        return TemperaturasMapper.toResponse(repo.save(l));
    }

    /** DELETE: borra la lectura y las alertas que había originado. */
    @Transactional
    public void eliminar(Long id) {
        Lectura l = buscar(id);
        alertaRepo.deleteByLecturaId(id);
        repo.delete(l);
    }

    private Lectura buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Lectura " + id + " no existe"));
    }
}
