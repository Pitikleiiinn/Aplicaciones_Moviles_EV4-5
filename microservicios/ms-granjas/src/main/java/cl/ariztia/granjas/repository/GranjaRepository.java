package cl.ariztia.granjas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.granjas.model.Granja;

/** [REPOSITORIO] CRUD de Granja (findAll, findById, save, delete... ya vienen incluidos). */
public interface GranjaRepository extends JpaRepository<Granja, Long> {
}
