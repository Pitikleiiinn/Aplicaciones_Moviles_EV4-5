package cl.ariztia.temperaturas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.temperaturas.model.LineaBebedero;

/** [REPOSITORIO] Líneas: listado ordenado como en la app y búsqueda por (granja, galpón, línea). */
public interface LineaBebederoRepository extends JpaRepository<LineaBebedero, Long> {
    List<LineaBebedero> findAllByOrderByGranjaAscGalponAscLineaAsc();
    Optional<LineaBebedero> findByGranjaAndGalponAndLinea(String granja, String galpon, String linea);
}
