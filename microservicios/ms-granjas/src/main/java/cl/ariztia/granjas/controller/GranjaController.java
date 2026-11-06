package cl.ariztia.granjas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cl.ariztia.granjas.dto.GalponDto;
import cl.ariztia.granjas.dto.GranjaDto;
import cl.ariztia.granjas.service.GalponService;
import cl.ariztia.granjas.service.GranjaService;
import jakarta.validation.Valid;

/**
 * [CONTROLADOR REST] Granjas (puerto 8081)
 *   GET    /api/granjas                 -> listar
 *   GET    /api/granjas/{id}            -> obtener (incluye lat/lon para el clima)
 *   POST   /api/granjas                 -> crear
 *   PUT    /api/granjas/{id}            -> actualizar
 *   DELETE /api/granjas/{id}            -> eliminar (borra sus galpones y líneas)
 *   GET    /api/granjas/{id}/galpones   -> galpones de la granja
 */
@RestController
@RequestMapping("/api/granjas")
public class GranjaController {

    private final GranjaService service;
    private final GalponService galponService;

    public GranjaController(GranjaService service, GalponService galponService) {
        this.service = service;
        this.galponService = galponService;
    }

    @GetMapping
    public List<GranjaDto> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public GranjaDto obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GranjaDto crear(@Valid @RequestBody GranjaDto dto) { return service.crear(dto); }

    @PutMapping("/{id}")
    public GranjaDto actualizar(@PathVariable Long id, @Valid @RequestBody GranjaDto dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }

    @GetMapping("/{id}/galpones")
    public List<GalponDto> galpones(@PathVariable Long id) { return galponService.listarPorGranja(id); }
}
