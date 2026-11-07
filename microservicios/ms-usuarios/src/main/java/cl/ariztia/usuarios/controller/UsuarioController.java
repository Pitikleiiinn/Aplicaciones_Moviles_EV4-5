package cl.ariztia.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cl.ariztia.usuarios.dto.LoginRequest;
import cl.ariztia.usuarios.dto.UsuarioRequest;
import cl.ariztia.usuarios.dto.UsuarioResponse;
import cl.ariztia.usuarios.service.UsuarioService;
import jakarta.validation.Valid;

/**
 * [CONTROLADOR REST] Usuarios (puerto 8084)
 *   POST   /api/usuarios/login   -> iniciar sesión (usuario + clave)
 *   GET    /api/usuarios         -> listar
 *   GET    /api/usuarios/{id}    -> obtener
 *   POST   /api/usuarios         -> crear
 *   PUT    /api/usuarios/{id}    -> actualizar
 *   DELETE /api/usuarios/{id}    -> eliminar
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest req) { return service.login(req); }

    @GetMapping
    public List<UsuarioResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public UsuarioResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest req) { return service.crear(req); }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
