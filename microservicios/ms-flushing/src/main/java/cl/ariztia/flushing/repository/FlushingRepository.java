package cl.ariztia.flushing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.flushing.model.Flushing;

/** [REPOSITORIO] Flushing: listados ordenados del más reciente al más antiguo. */
public interface FlushingRepository extends JpaRepository<Flushing, Long> {
    List<Flushing> findAllByOrderByFechaRegistroDescIdDesc();
    List<Flushing> findByLineaIdOrderByFechaRegistroDescIdDesc(Long lineaId);
}
