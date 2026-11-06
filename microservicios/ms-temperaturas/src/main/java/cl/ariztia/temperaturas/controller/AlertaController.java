package cl.ariztia.temperaturas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import cl.ariztia.temperaturas.dto.AlertaResponse;
import cl.ariztia.temperaturas.service.AlertaService;

/**
 * [CONTROLADOR REST] Alertas (puerto 8082)
 *   GET /api/alertas?activas=true      -> alertas (por defecto solo las activas)
 *   GET /api/alertas/linea/{lineaId}   -> alertas de una línea
 *   PUT /api/alertas/{id}/atender      -> marcar como atendida
 */
@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaService service;

    public AlertaController(AlertaService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlertaResponse> listar(@RequestParam(defaultValue = "true") boolean activas) {
        return service.listar(activas);
    }

    @GetMapping("/linea/{lineaId}")
    public List<AlertaResponse> porLinea(@PathVariable Long lineaId) { return service.listarPorLinea(lineaId); }

    @PutMapping("/{id}/atender")
    public AlertaResponse atender(@PathVariable Long id) { return service.atender(id); }
}
