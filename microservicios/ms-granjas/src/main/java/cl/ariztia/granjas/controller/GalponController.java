package cl.ariztia.granjas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cl.ariztia.granjas.dto.GalponDto;
import cl.ariztia.granjas.dto.LineaDto;
import cl.ariztia.granjas.service.GalponService;
import cl.ariztia.granjas.service.LineaService;
import jakarta.validation.Valid;

/**
 * [CONTROLADOR REST] Galpones (puerto 8081)
 *   GET    /api/galpones/{id}         -> obtener
 *   POST   /api/galpones              -> crear (body con granjaId)
 *   PUT    /api/galpones/{id}         -> actualizar
 *   DELETE /api/galpones/{id}         -> eliminar
 *   GET    /api/galpones/{id}/lineas  -> líneas del galpón
 */
@RestController
@RequestMapping("/api/galpones")
public class GalponController {

    private final GalponService service;
    private final LineaService lineaService;

    public GalponController(GalponService service, LineaService lineaService) {
        this.service = service;
        this.lineaService = lineaService;
    }

    @GetMapping("/{id}")
    public GalponDto obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GalponDto crear(@Valid @RequestBody GalponDto dto) { return service.crear(dto); }

    @PutMapping("/{id}")
    public GalponDto actualizar(@PathVariable Long id, @Valid @RequestBody GalponDto dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }

    @GetMapping("/{id}/lineas")
    public List<LineaDto> lineas(@PathVariable Long id) { return lineaService.listarPorGalpon(id); }
}
