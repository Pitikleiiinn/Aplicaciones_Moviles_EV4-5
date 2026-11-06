package cl.ariztia.temperaturas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cl.ariztia.temperaturas.dto.LecturaRequest;
import cl.ariztia.temperaturas.dto.LecturaResponse;
import cl.ariztia.temperaturas.service.LecturaService;
import jakarta.validation.Valid;

/**
 * [CONTROLADOR REST] Lecturas de temperatura (puerto 8082)
 *   POST   /api/temperaturas                         -> registrar lectura (calcula estado y alerta)
 *   GET    /api/temperaturas/linea/{lineaId}         -> historial de la línea
 *   GET    /api/temperaturas/linea/{lineaId}/ultima  -> última lectura de la línea
 *   GET    /api/temperaturas/ultimas?lineaIds=1&lineaIds=2 -> última lectura de varias líneas
 *   PUT    /api/temperaturas/{id}                    -> corregir lectura
 *   DELETE /api/temperaturas/{id}                    -> eliminar lectura
 */
@RestController
@RequestMapping("/api/temperaturas")
public class TemperaturaController {

    private final LecturaService service;

    public TemperaturaController(LecturaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LecturaResponse registrar(@Valid @RequestBody LecturaRequest req) { return service.registrar(req); }

    @GetMapping("/linea/{lineaId}")
    public List<LecturaResponse> historial(@PathVariable Long lineaId) { return service.historial(lineaId); }

    @GetMapping("/linea/{lineaId}/ultima")
    public LecturaResponse ultima(@PathVariable Long lineaId) { return service.ultima(lineaId); }

    @GetMapping("/ultimas")
    public List<LecturaResponse> ultimas(@RequestParam List<Long> lineaIds) { return service.ultimas(lineaIds); }

    @PutMapping("/{id}")
    public LecturaResponse actualizar(@PathVariable Long id, @Valid @RequestBody LecturaRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
