package cl.ariztia.temperaturas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ariztia.temperaturas.model.Alerta;

/** [REPOSITORIO] Alertas: todas, solo activas, por línea; y borrado por lectura. */
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findAllByOrderByFechaHoraDesc();
    List<Alerta> findByAtendidaFalseOrderByFechaHoraDesc();
    List<Alerta> findByLineaIdOrderByFechaHoraDesc(Long lineaId);
    void deleteByLecturaId(Long lecturaId);
}
