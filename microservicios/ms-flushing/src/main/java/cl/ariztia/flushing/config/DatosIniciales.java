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
            service.crear(new FlushingRequest("Granja Melipilla", "Galpón 2", "Línea 1", "Carlos Soto",
                    "Temperatura elevada", "Flushing realizado por temperatura crítica."));
            service.crear(new FlushingRequest("Granja Melipilla", "Galpón 1", "Línea 2", "Carlos Soto",
                    "Limpieza preventiva", "Limpieza de rutina sin novedades."));
        };
    }
}
