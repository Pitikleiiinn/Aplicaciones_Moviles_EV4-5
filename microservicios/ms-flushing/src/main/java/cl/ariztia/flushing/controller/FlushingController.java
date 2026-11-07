package cl.ariztia.flushing.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cl.ariztia.flushing.dto.FlushingRequest;
import cl.ariztia.flushing.dto.FlushingResponse;
import cl.ariztia.flushing.service.FlushingService;
import jakarta.validation.Valid;

/**
 * [CONTROLADOR REST] Flushing (puerto 8083)
 *   GET    /api/flushing        -> historial
 *   GET    /api/flushing/{id}   -> uno
 *   POST   /api/flushing        -> registrar
 *   PUT    /api/flushing/{id}   -> corregir
 *   DELETE /api/flushing/{id}   -> eliminar
 */
@RestController
@RequestMapping("/api/flushing")
public class FlushingController {

    private final FlushingService service;

    public FlushingController(FlushingService service) {
        this.service = service;
    }

    @GetMapping
    public List<FlushingResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public FlushingResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlushingResponse crear(@Valid @RequestBody FlushingRequest req) { return service.crear(req); }

    @PutMapping("/{id}")
    public FlushingResponse actualizar(@PathVariable Long id, @Valid @RequestBody FlushingRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
