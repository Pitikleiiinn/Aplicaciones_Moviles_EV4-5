package cl.ariztia.temperaturas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.ariztia.temperaturas.dto.AlertaResponse;
import cl.ariztia.temperaturas.exception.RecursoNoEncontradoException;
import cl.ariztia.temperaturas.mapper.TemperaturasMapper;
import cl.ariztia.temperaturas.model.Alerta;
import cl.ariztia.temperaturas.model.EstadoLinea;
import cl.ariztia.temperaturas.model.Lectura;
import cl.ariztia.temperaturas.repository.AlertaRepository;

/** [SERVICIO] Generación y consulta de alertas. */
@Service
public class AlertaService {

    private final AlertaRepository repo;

    public AlertaService(AlertaRepository repo) {
        this.repo = repo;
    }

    /** Crea una alerta si la lectura no está NORMAL. Se llama desde LecturaService al registrar. */
    @Transactional
    public void generarSiCorresponde(Lectura lectura) {
        if (lectura.getEstado() == EstadoLinea.NORMAL) return;
        Alerta a = new Alerta();
        a.setLineaId(lectura.getLineaId());
        a.setLineaNombre(lectura.getLineaNombre());
        a.setLecturaId(lectura.getId());
        a.setEstado(lectura.getEstado());
        a.setFechaHora(LocalDateTime.now());
        a.setMensaje(String.format(Locale.ROOT, "Temperatura %s: %.1f °C. Evaluar realizar flushing.",
                lectura.getEstado() == EstadoLinea.CRITICO ? "crítica" : "en advertencia", lectura.getTemperatura()));
        repo.save(a);
    }

    @Transactional(readOnly = true)
    public List<AlertaResponse> listar(boolean soloActivas) {
        List<Alerta> alertas = soloActivas ? repo.findByAtendidaFalseOrderByFechaHoraDesc() : repo.findAllByOrderByFechaHoraDesc();
        return alertas.stream().map(TemperaturasMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AlertaResponse> listarPorLinea(Long lineaId) {
        return repo.findByLineaIdOrderByFechaHoraDesc(lineaId).stream().map(TemperaturasMapper::toResponse).toList();
    }

    /** Marca la alerta como atendida (ya no aparece entre las activas). */
    @Transactional
    public AlertaResponse atender(Long id) {
        Alerta a = repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Alerta " + id + " no existe"));
        a.setAtendida(true);
        return TemperaturasMapper.toResponse(repo.save(a));
    }
}
