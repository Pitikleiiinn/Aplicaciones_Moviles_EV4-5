package cl.ariztia.flushing.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/** [ENTIDAD JPA] Registro de un flushing (purga de líneas). Mismos campos que FlusingEntity de la app. */
@Entity
@Table(name = "registros_flushing")
public class Flushing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String granja;

    @Column(nullable = false)
    private String galpon;

    @Column(nullable = false)
    private String linea;

    @Column(nullable = false)
    private String responsable;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false, length = 500)
    private String observaciones;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    /** La fecha/hora la pone el servidor al insertar */
    @PrePersist
    void antesDeGuardar() {
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getGranja() { return granja; }
    public void setGranja(String granja) { this.granja = granja; }
    public String getGalpon() { return galpon; }
    public void setGalpon(String galpon) { this.galpon = galpon; }
    public String getLinea() { return linea; }
    public void setLinea(String linea) { this.linea = linea; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
