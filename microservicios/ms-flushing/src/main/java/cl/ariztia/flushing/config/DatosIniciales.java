package cl.ariztia.flushing.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.ariztia.flushing.dto.FlushingRequest;
import cl.ariztia.flushing.repository.FlushingRepository;
import cl.ariztia.flushing.service.FlushingService;

/** [DATOS DE PRUEBA] Dos flushing de ejemplo para que el historial no parta vacío. */
@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner cargarFlushing(FlushingRepository repo, FlushingService service) {
        return args -> {
            if (repo.count() > 0) return;
            service.crear(new FlushingRequest(3L, "Los Aromos", "Galpón 2", "Línea A", "Carlos Operario", 15,
                    "Flushing por temperatura crítica. Se normalizó tras la limpieza."));
            service.crear(new FlushingRequest(7L, "El Roble", "Galpón 2", "Línea A", "Carlos Operario", 20,
                    "Flushing preventivo de rutina."));
        };
    }
}
