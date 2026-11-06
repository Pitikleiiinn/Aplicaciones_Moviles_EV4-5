package cl.ariztia.granjas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.granjas.model.Galpon;

/** [REPOSITORIO] Galpones; permite listar los de una granja. */
public interface GalponRepository extends JpaRepository<Galpon, Long> {
    List<Galpon> findByGranjaIdOrderByNombre(Long granjaId);
}
