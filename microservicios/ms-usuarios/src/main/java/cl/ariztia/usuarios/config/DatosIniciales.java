package cl.ariztia.usuarios.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.ariztia.usuarios.model.Rol;
import cl.ariztia.usuarios.model.Usuario;
import cl.ariztia.usuarios.repository.UsuarioRepository;

/** [DATOS DE PRUEBA] Los mismos usuarios ficticios que tenía la app: operario / supervisor, clave 1234. */
@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner cargarUsuarios(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() > 0) return;
            crear(repo, encoder, "operario", "Operario", "1234", Rol.OPERARIO);
            crear(repo, encoder, "supervisor", "Supervisor", "1234", Rol.SUPERVISOR);
        };
    }

    private void crear(UsuarioRepository repo, PasswordEncoder enc, String usuario, String nombre, String clave, Rol rol) {
        Usuario u = new Usuario();
        u.setUsuario(usuario);
        u.setNombre(nombre);
        u.setClaveHash(enc.encode(clave));
        u.setRol(rol);
        repo.save(u);
    }
}
