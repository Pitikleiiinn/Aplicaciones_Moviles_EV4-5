package cl.ariztia.flushing.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * [ENTIDAD JPA] Registro de un flushing (limpieza del sistema de bebederos) hecho por un operario.
 * lineaId referencia a ms-granjas por id; los nombres se guardan como copia para el historial.
 */
@Entity
@Table(name = "flushing")
public class Flushing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lineaId;

    private String granjaNombre;
    private String galponNombre;
    private String lineaNombre;

    @Column(nullable = false)
    private String responsable;

    @Column(nullable = false)
    private Integer duracionMinutos;

    @Column(nullable = false, length = 500)
    private String observacion;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    /** Se asigna sola la fecha/hora al insertar si no viene definida */
    @PrePersist
    void antesDeGuardar() {
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getLineaId() { return lineaId; }
    public void setLineaId(Long lineaId) { this.lineaId = lineaId; }
    public String getGranjaNombre() { return granjaNombre; }
    public void setGranjaNombre(String granjaNombre) { this.granjaNombre = granjaNombre; }
    public String getGalponNombre() { return galponNombre; }
    public void setGalponNombre(String galponNombre) { this.galponNombre = galponNombre; }
    public String getLineaNombre() { return lineaNombre; }
    public void setLineaNombre(String lineaNombre) { this.lineaNombre = lineaNombre; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
