package cl.ariztia.temperaturas.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/** [ENTIDAD JPA] Alerta generada automáticamente cuando una lectura queda en ADVERTENCIA o CRITICO. */
@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lineaId;

    private String lineaNombre;

    /** Lectura que originó la alerta */
    @Column(nullable = false)
    private Long lecturaId;

    @Column(nullable = false)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLinea estado;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    /** false = activa; true = ya fue atendida (ej. se hizo flushing) */
    @Column(nullable = false)
    private boolean atendida = false;

    public Long getId() { return id; }
    public Long getLineaId() { return lineaId; }
    public void setLineaId(Long lineaId) { this.lineaId = lineaId; }
    public String getLineaNombre() { return lineaNombre; }
    public void setLineaNombre(String lineaNombre) { this.lineaNombre = lineaNombre; }
    public Long getLecturaId() { return lecturaId; }
    public void setLecturaId(Long lecturaId) { this.lecturaId = lecturaId; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public EstadoLinea getEstado() { return estado; }
    public void setEstado(EstadoLinea estado) { this.estado = estado; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public boolean isAtendida() { return atendida; }
    public void setAtendida(boolean atendida) { this.atendida = atendida; }
}
