package cl.ariztia.usuarios.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.ariztia.usuarios.dto.LoginRequest;
import cl.ariztia.usuarios.dto.UsuarioRequest;
import cl.ariztia.usuarios.dto.UsuarioResponse;
import cl.ariztia.usuarios.exception.RecursoNoEncontradoException;
import cl.ariztia.usuarios.mapper.UsuarioMapper;
import cl.ariztia.usuarios.model.Usuario;
import cl.ariztia.usuarios.repository.UsuarioRepository;

/** [SERVICIO] Lógica de negocio de usuarios: login y CRUD. */
@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    /** [LOGIN] Valida correo + contraseña. Mismo mensaje para ambos fallos (no revela cuál falló). */
    @Transactional(readOnly = true)
    public UsuarioResponse login(LoginRequest req) {
        Usuario u = repo.findByEmailIgnoreCase(req.email().trim())
                .filter(x -> encoder.matches(req.password(), x.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos"));
        return UsuarioMapper.toResponse(u);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return repo.findAll().stream().map(UsuarioMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long id) {
        return UsuarioMapper.toResponse(buscar(id));
    }

    @Transactional
    public UsuarioResponse crear(UsuarioRequest req) {
        if (req.password() == null || req.password().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe tener al menos 4 caracteres");
        }
        if (repo.existsByEmailIgnoreCase(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }
        Usuario u = new Usuario();
        aplicar(u, req);
        u.setPasswordHash(encoder.encode(req.password()));
        return UsuarioMapper.toResponse(repo.save(u));
    }

    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest req) {
        Usuario u = buscar(id);
        aplicar(u, req);
        if (req.password() != null && !req.password().isBlank()) {
            u.setPasswordHash(encoder.encode(req.password()));
        }
        return UsuarioMapper.toResponse(repo.save(u));
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    private Usuario buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Usuario " + id + " no existe"));
    }

    private void aplicar(Usuario u, UsuarioRequest req) {
        u.setNombre(req.nombre().trim());
        u.setEmail(req.email().trim().toLowerCase());
        u.setRol(req.rol());
    }
}
