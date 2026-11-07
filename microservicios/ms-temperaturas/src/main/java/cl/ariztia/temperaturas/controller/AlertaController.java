package cl.ariztia.temperaturas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.ariztia.temperaturas.dto.LineaResponse;
import cl.ariztia.temperaturas.dto.ResumenResponse;
import cl.ariztia.temperaturas.service.LineaService;

/**
 * [CONTROLADOR REST] Alertas y resumen (puerto 8082)
 *   GET /api/alertas  -> líneas en Advertencia o Crítico
 *   GET /api/resumen  -> métricas del supervisor (totales por estado)
 */
@RestController
@RequestMapping("/api")
public class AlertaController {

    private final LineaService service;

    public AlertaController(LineaService service) {
        this.service = service;
    }

    @GetMapping("/alertas")
    public List<LineaResponse> alertas() { return service.alertas(); }

    @GetMapping("/resumen")
    public ResumenResponse resumen() { return service.resumen(); }
}
