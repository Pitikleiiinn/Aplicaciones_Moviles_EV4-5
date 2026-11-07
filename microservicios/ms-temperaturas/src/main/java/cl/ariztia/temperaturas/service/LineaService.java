package cl.ariztia.temperaturas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.ariztia.temperaturas.dto.LecturaResponse;
import cl.ariztia.temperaturas.dto.LineaResponse;
import cl.ariztia.temperaturas.dto.ResumenResponse;
import cl.ariztia.temperaturas.dto.TemperaturaRequest;
import cl.ariztia.temperaturas.exception.RecursoNoEncontradoException;
import cl.ariztia.temperaturas.mapper.TemperaturasMapper;
import cl.ariztia.temperaturas.model.EstadoLinea;
import cl.ariztia.temperaturas.model.Lectura;
import cl.ariztia.temperaturas.model.LineaBebedero;
import cl.ariztia.temperaturas.repository.LecturaRepository;
import cl.ariztia.temperaturas.repository.LineaBebederoRepository;

/** [SERVICIO] Registro de temperaturas, consulta de líneas, alertas y resumen del supervisor. */
@Service
public class LineaService {

    private final LineaBebederoRepository lineas;
    private final LecturaRepository lecturas;
    private final ClasificadorTemperatura clasificador;

    public LineaService(LineaBebederoRepository lineas, LecturaRepository lecturas, ClasificadorTemperatura clasificador) {
        this.lineas = lineas;
        this.lecturas = lecturas;
        this.clasificador = clasificador;
    }

    /**
     * CREATE / UPDATE (upsert): si la línea (granja, galpón, línea) ya existe se actualiza su temperatura;
     * si no, se crea. En ambos casos se guarda además la medición en el historial.
     */
    @Transactional
    public LineaResponse registrarTemperatura(TemperaturaRequest req) {
        LocalDateTime ahora = LocalDateTime.now();
        LineaBebedero linea = lineas.findByGranjaAndGalponAndLinea(req.granja(), req.galpon(), req.linea())
                .orElseGet(() -> {
                    LineaBebedero nueva = new LineaBebedero();
                    nueva.setGranja(req.granja());
                    nueva.setGalpon(req.galpon());
                    nueva.setLinea(req.linea());
                    return nueva;
                });
        linea.setTemperatura(req.temperatura());
        linea.setFechaActualizacion(ahora);
        LineaBebedero guardada = lineas.save(linea);

        EstadoLinea estado = clasificador.clasificar(req.temperatura());
        Lectura lectura = new Lectura();
        lectura.setLineaId(guardada.getId());
        lectura.setTemperatura(req.temperatura());
        lectura.setEstado(estado);
        lectura.setFechaHora(ahora);
        lecturas.save(lectura);

        return TemperaturasMapper.toResponse(guardada, estado);
    }

    /** READ: todas las líneas con su última temperatura. */
    @Transactional(readOnly = true)
    public List<LineaResponse> listar() {
        return lineas.findAllByOrderByGranjaAscGalponAscLineaAsc().stream().map(this::aRespuesta).toList();
    }

    @Transactional(readOnly = true)
    public LineaResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    /** READ: alertas = líneas en Advertencia o Crítico (lo que muestra la pantalla de alertas). */
    @Transactional(readOnly = true)
    public List<LineaResponse> alertas() {
        return listar().stream().filter(l -> !EstadoLinea.NORMAL.getEtiqueta().equals(l.estado())).toList();
    }

    /** READ: métricas del panel del supervisor. */
    @Transactional(readOnly = true)
    public ResumenResponse resumen() {
        List<LineaResponse> todas = listar();
        int normales = (int) todas.stream().filter(l -> EstadoLinea.NORMAL.getEtiqueta().equals(l.estado())).count();
        int advertencias = (int) todas.stream().filter(l -> EstadoLinea.ADVERTENCIA.getEtiqueta().equals(l.estado())).count();
        int criticas = (int) todas.stream().filter(l -> EstadoLinea.CRITICO.getEtiqueta().equals(l.estado())).count();
        return new ResumenResponse(todas.size(), normales, advertencias, criticas);
    }

    /** READ: historial de mediciones de una línea. */
    @Transactional(readOnly = true)
    public List<LecturaResponse> historial(Long lineaId) {
        buscar(lineaId); // 404 si no existe
        return lecturas.findByLineaIdOrderByFechaHoraDescIdDesc(lineaId).stream().map(TemperaturasMapper::toResponse).toList();
    }

    /** DELETE: borra la línea y su historial. */
    @Transactional
    public void eliminar(Long id) {
        LineaBebedero l = buscar(id);
        lecturas.deleteByLineaId(id);
        lineas.delete(l);
    }

    private LineaBebedero buscar(Long id) {
        return lineas.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Línea " + id + " no existe"));
    }

    private LineaResponse aRespuesta(LineaBebedero l) {
        return TemperaturasMapper.toResponse(l, clasificador.clasificar(l.getTemperatura()));
    }
}
