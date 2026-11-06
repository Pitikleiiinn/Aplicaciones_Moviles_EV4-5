package cl.ariztia.granjas.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/** [ENTIDAD JPA] Nivel 2: un Galpón pertenece a una Granja y tiene varias Líneas de bebederos. */
@Entity
@Table(name = "galpones")
public class Galpon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    /** Relación N -> 1 (clave foránea granja_id) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "granja_id")
    private Granja granja;

    @OneToMany(mappedBy = "galpon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaBebedero> lineas = new ArrayList<>();

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Granja getGranja() { return granja; }
    public void setGranja(Granja granja) { this.granja = granja; }
    public List<LineaBebedero> getLineas() { return lineas; }
}
