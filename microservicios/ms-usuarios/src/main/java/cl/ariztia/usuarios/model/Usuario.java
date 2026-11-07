package cl.ariztia.usuarios.model;

import jakarta.persistence.*;

/** [ENTIDAD JPA] Tabla "usuarios". La clave se guarda cifrada (BCrypt), nunca en texto plano. */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario para iniciar sesión (único, en minúsculas). Ej: "operario" */
    @Column(nullable = false, unique = true)
    private String usuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String claveHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getClaveHash() { return claveHash; }
    public void setClaveHash(String claveHash) { this.claveHash = claveHash; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
