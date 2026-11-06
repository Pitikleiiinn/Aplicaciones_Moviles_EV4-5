package cl.ariztia.flushing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * [ARRANQUE] Punto de entrada del microservicio ms-flushing (puerto 8083).
 * Registro de acciones de flushing (limpieza de bebederos)
 */
@SpringBootApplication
public class FlushingApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlushingApplication.class, args);
    }
}
