package cl.ariztia.temperaturas.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.ariztia.temperaturas.dto.TemperaturaRequest;
import cl.ariztia.temperaturas.repository.LineaBebederoRepository;
import cl.ariztia.temperaturas.service.LineaService;

/** [DATOS DE PRUEBA] Las mismas 3 líneas de demostración que tenía la app (22,5 / 28,0 / 31,5 °C). */
@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner cargarLineas(LineaBebederoRepository repo, LineaService service) {
        return args -> {
            if (repo.count() > 0) return;
            service.registrarTemperatura(new TemperaturaRequest("Granja Melipilla", "Galpón 1", "Línea 1", 22.5));
            service.registrarTemperatura(new TemperaturaRequest("Granja Melipilla", "Galpón 1", "Línea 2", 28.0));
            service.registrarTemperatura(new TemperaturaRequest("Granja Melipilla", "Galpón 2", "Línea 1", 31.5));
        };
    }
}
