package cl.ariztia.temperaturas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * [ARRANQUE] Punto de entrada del microservicio ms-temperaturas (puerto 8082).
 * Temperatura por granja/galpón/línea, estado, alertas y resumen del supervisor
 */
@SpringBootApplication
public class TemperaturasApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemperaturasApplication.class, args);
    }
}
