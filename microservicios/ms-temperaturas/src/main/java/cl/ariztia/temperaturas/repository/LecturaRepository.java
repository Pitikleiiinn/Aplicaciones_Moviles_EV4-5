package cl.ariztia.temperaturas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.temperaturas.model.Lectura;

/** [REPOSITORIO] Historial de mediciones de una línea (más reciente primero). */
public interface LecturaRepository extends JpaRepository<Lectura, Long> {
    List<Lectura> findByLineaIdOrderByFechaHoraDescIdDesc(Long lineaId);
    void deleteByLineaId(Long lineaId);
}
