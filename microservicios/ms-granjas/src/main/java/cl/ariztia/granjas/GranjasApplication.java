package cl.ariztia.granjas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * [ARRANQUE] Punto de entrada del microservicio ms-granjas (puerto 8081).
 * Catálogo: Granjas > Galpones > Líneas de bebederos
 */
@SpringBootApplication
public class GranjasApplication {
    public static void main(String[] args) {
        SpringApplication.run(GranjasApplication.class, args);
    }
}
