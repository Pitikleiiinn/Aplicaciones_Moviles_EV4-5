package cl.ariztia.usuarios.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.ariztia.usuarios.model.Rol;
import cl.ariztia.usuarios.model.Usuario;
import cl.ariztia.usuarios.repository.UsuarioRepository;

/** [DATOS DE PRUEBA] El caso exige datos ficticios. Se crean 2 usuarios solo si la tabla está vacía. */
@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner cargarUsuarios(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() > 0) return;
            crear(repo, encoder, "Carlos Operario", "operario@ariztia.cl", "1234", Rol.OPERARIO);
            crear(repo, encoder, "Andrea Supervisora", "supervisor@ariztia.cl", "1234", Rol.SUPERVISOR);
        };
    }

    private void crear(UsuarioRepository repo, PasswordEncoder enc, String nombre, String email, String pass, Rol rol) {
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPasswordHash(enc.encode(pass));
        u.setRol(rol);
        repo.save(u);
    }
}
