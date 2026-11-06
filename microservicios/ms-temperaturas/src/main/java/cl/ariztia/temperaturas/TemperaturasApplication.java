package cl.ariztia.temperaturas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * [ARRANQUE] Punto de entrada del microservicio ms-temperaturas (puerto 8082).
 * Lecturas de temperatura, estado de la línea y alertas
 */
@SpringBootApplication
public class TemperaturasApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemperaturasApplication.class, args);
    }
}
