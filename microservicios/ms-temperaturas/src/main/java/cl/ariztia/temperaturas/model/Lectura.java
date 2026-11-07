package cl.ariztia.temperaturas.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/** [ENTIDAD JPA] Historial: cada medición registrada queda guardada aunque la línea se actualice después. */
@Entity
@Table(name = "lecturas")
public class Lectura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lineaId;

    @Column(nullable = false)
    private Double temperatura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLinea estado;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    public Long getId() { return id; }
    public Long getLineaId() { return lineaId; }
    public void setLineaId(Long lineaId) { this.lineaId = lineaId; }
    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    public EstadoLinea getEstado() { return estado; }
    public void setEstado(EstadoLinea estado) { this.estado = estado; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
