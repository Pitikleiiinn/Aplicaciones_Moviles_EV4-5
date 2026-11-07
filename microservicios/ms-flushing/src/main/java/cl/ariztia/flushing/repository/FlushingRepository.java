package cl.ariztia.flushing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.flushing.model.Flushing;

/** [REPOSITORIO] Historial ordenado del más nuevo al más antiguo (como en la app: ORDER BY id DESC). */
public interface FlushingRepository extends JpaRepository<Flushing, Long> {
    List<Flushing> findAllByOrderByIdDesc();
}
