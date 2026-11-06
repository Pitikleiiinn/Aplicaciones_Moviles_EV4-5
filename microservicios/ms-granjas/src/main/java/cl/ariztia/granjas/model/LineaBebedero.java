package cl.ariztia.granjas.model;

import jakarta.persistence.*;

/** [ENTIDAD JPA] Nivel 3: línea de bebederos (el punto donde se mide la temperatura del agua). */
@Entity
@Table(name = "lineas_bebedero")
public class LineaBebedero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "galpon_id")
    private Galpon galpon;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Galpon getGalpon() { return galpon; }
    public void setGalpon(Galpon galpon) { this.galpon = galpon; }
}
