package cl.ariztia.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.usuarios.model.Usuario;

/** [REPOSITORIO] Spring Data genera el SQL a partir del nombre de los métodos. */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuarioIgnoreCase(String usuario);
    boolean existsByUsuarioIgnoreCase(String usuario);
}
