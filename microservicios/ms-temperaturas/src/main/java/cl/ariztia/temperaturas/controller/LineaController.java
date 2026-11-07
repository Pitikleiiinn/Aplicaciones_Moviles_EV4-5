package cl.ariztia.temperaturas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cl.ariztia.temperaturas.dto.LecturaResponse;
import cl.ariztia.temperaturas.dto.LineaResponse;
import cl.ariztia.temperaturas.dto.TemperaturaRequest;
import cl.ariztia.temperaturas.service.LineaService;
import jakarta.validation.Valid;

/**
 * [CONTROLADOR REST] Líneas de bebederos (puerto 8082)
 *   GET    /api/lineas                  -> todas las líneas con su última temperatura
 *   GET    /api/lineas/{id}             -> una línea
 *   POST   /api/lineas/temperatura      -> registrar temperatura (crea o actualiza la línea)
 *   GET    /api/lineas/{id}/lecturas    -> historial de la línea
 *   DELETE /api/lineas/{id}             -> eliminar línea
 */
@RestController
@RequestMapping("/api/lineas")
public class LineaController {

    private final LineaService service;

    public LineaController(LineaService service) {
        this.service = service;
    }

    @GetMapping
    public List<LineaResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public LineaResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping("/temperatura")
    @ResponseStatus(HttpStatus.CREATED)
    public LineaResponse registrar(@Valid @RequestBody TemperaturaRequest req) { return service.registrarTemperatura(req); }

    @GetMapping("/{id}/lecturas")
    public List<LecturaResponse> historial(@PathVariable Long id) { return service.historial(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
