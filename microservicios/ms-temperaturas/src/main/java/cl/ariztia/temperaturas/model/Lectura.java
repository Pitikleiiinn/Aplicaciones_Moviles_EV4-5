package cl.ariztia.temperaturas.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * [ENTIDAD JPA] Una medición de temperatura de una línea de bebederos.
 * lineaId referencia a ms-granjas SOLO por id (los microservicios no comparten tablas).
 * lineaNombre es una copia descriptiva ("Granja > Galpón > Línea") para mostrar sin consultar otro servicio.
 */
@Entity
@Table(name = "lecturas")
public class Lectura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lineaId;

    private String lineaNombre;

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
    public String getLineaNombre() { return lineaNombre; }
    public void setLineaNombre(String lineaNombre) { this.lineaNombre = lineaNombre; }
    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    public EstadoLinea getEstado() { return estado; }
    public void setEstado(EstadoLinea estado) { this.estado = estado; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
