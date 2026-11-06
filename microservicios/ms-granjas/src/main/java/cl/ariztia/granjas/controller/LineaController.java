package cl.ariztia.granjas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cl.ariztia.granjas.dto.LineaDto;
import cl.ariztia.granjas.service.LineaService;
import jakarta.validation.Valid;

/**
 * [CONTROLADOR REST] Líneas de bebederos (puerto 8081)
 *   GET    /api/lineas/{id}  -> obtener (con nombre de galpón y granja)
 *   POST   /api/lineas       -> crear (body con galponId)
 *   PUT    /api/lineas/{id}  -> actualizar
 *   DELETE /api/lineas/{id}  -> eliminar
 */
@RestController
@RequestMapping("/api/lineas")
public class LineaController {

    private final LineaService service;

    public LineaController(LineaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public LineaDto obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LineaDto crear(@Valid @RequestBody LineaDto dto) { return service.crear(dto); }

    @PutMapping("/{id}")
    public LineaDto actualizar(@PathVariable Long id, @Valid @RequestBody LineaDto dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
