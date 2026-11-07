package cl.ariztia.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * [ARRANQUE] Punto de entrada del microservicio ms-usuarios (puerto 8084).
 * Inicio de sesión y gestión de usuarios (roles OPERARIO / SUPERVISOR)
 */
@SpringBootApplication
public class UsuariosApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsuariosApplication.class, args);
    }
}
