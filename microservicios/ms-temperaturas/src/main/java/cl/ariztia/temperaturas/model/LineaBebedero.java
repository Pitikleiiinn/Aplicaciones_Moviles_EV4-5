package cl.ariztia.temperaturas.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * [ENTIDAD JPA] Una línea de bebederos identificada por (granja, galpón, línea) con su ÚLTIMA temperatura.
 * La combinación es única: registrar una temperatura para una línea existente la actualiza (upsert).
 */
@Entity
@Table(name = "lineas_bebedero",
       uniqueConstraints = @UniqueConstraint(columnNames = {"granja", "galpon", "linea"}))
public class LineaBebedero {

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
    private Double temperatura;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getId() { return id; }
    public String getGranja() { return granja; }
    public void setGranja(String granja) { this.granja = granja; }
    public String getGalpon() { return galpon; }
    public void setGalpon(String galpon) { this.galpon = galpon; }
    public String getLinea() { return linea; }
    public void setLinea(String linea) { this.linea = linea; }
    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
