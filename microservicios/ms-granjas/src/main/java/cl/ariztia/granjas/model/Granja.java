package cl.ariztia.granjas.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/** [ENTIDAD JPA] Nivel 1 de la jerarquía: una Granja tiene varios Galpones. */
@Entity
@Table(name = "granjas")
public class Granja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String ubicacion;

    /** Coordenadas: la app las usa para consultar el clima exterior en la API externa (Open-Meteo) */
    private Double latitud;
    private Double longitud;

    /** Relación 1 -> N. Al borrar la granja se borran sus galpones (cascade + orphanRemoval). */
    @OneToMany(mappedBy = "granja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Galpon> galpones = new ArrayList<>();

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public List<Galpon> getGalpones() { return galpones; }
}
