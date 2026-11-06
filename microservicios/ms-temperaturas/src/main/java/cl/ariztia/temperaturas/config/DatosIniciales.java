package cl.ariztia.temperaturas.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.ariztia.temperaturas.dto.LecturaRequest;
import cl.ariztia.temperaturas.repository.LecturaRepository;
import cl.ariztia.temperaturas.service.LecturaService;

/**
 * [DATOS DE PRUEBA] Simula sensores: 3 lecturas por cada una de las 8 líneas creadas por ms-granjas
 * (ids 1..8). Pasa por LecturaService.registrar(), así también se generan alertas reales.
 */
@Configuration
public class DatosIniciales {

    private static final String[] NOMBRES = {
        "Los Aromos › Galpón 1 › Línea A", "Los Aromos › Galpón 1 › Línea B",
        "Los Aromos › Galpón 2 › Línea A", "Los Aromos › Galpón 2 › Línea B",
        "El Roble › Galpón 1 › Línea A",   "El Roble › Galpón 1 › Línea B",
        "El Roble › Galpón 2 › Línea A",   "El Roble › Galpón 2 › Línea B"
    };

    /** Cada fila = 3 lecturas (de la más antigua a la más nueva) de una línea */
    private static final double[][] TEMPERATURAS = {
        {21.0, 22.0, 22.5}, {24.0, 26.5, 27.8}, {28.0, 30.5, 31.4}, {20.5, 21.0, 21.2},
        {23.0, 23.8, 24.0}, {26.0, 28.5, 29.0}, {29.0, 32.0, 33.0}, {19.0, 19.5, 20.0}
    };

    @Bean
    CommandLineRunner cargarLecturas(LecturaRepository repo, LecturaService service) {
        return args -> {
            if (repo.count() > 0) return;
            for (int i = 0; i < NOMBRES.length; i++) {
                for (double t : TEMPERATURAS[i]) {
                    service.registrar(new LecturaRequest((long) (i + 1), NOMBRES[i], t));
                }
            }
        };
    }
}
