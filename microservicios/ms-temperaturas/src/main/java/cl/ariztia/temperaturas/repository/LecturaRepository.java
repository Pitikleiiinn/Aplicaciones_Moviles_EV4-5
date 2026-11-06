package cl.ariztia.temperaturas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.temperaturas.model.Lectura;

/** [REPOSITORIO] Lecturas: historial por línea (más reciente primero) y última lectura. */
public interface LecturaRepository extends JpaRepository<Lectura, Long> {
    List<Lectura> findByLineaIdOrderByFechaHoraDescIdDesc(Long lineaId);
    Optional<Lectura> findFirstByLineaIdOrderByFechaHoraDescIdDesc(Long lineaId);
}
