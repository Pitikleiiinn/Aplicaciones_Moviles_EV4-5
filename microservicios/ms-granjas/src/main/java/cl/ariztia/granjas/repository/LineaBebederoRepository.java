package cl.ariztia.granjas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.granjas.model.LineaBebedero;

/** [REPOSITORIO] Líneas de bebederos; permite listar las de un galpón. */
public interface LineaBebederoRepository extends JpaRepository<LineaBebedero, Long> {
    List<LineaBebedero> findByGalponIdOrderByNombre(Long galponId);
}
