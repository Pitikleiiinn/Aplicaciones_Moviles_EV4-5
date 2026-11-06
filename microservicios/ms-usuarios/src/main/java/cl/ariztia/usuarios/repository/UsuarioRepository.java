package cl.ariztia.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.usuarios.model.Usuario;

/** [REPOSITORIO] Acceso a datos. Spring Data genera el SQL a partir del nombre de los métodos. */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
