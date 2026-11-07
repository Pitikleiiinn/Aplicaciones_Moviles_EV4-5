package cl.ariztia.usuarios.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * [MANEJO DE ERRORES] Convierte excepciones en respuestas JSON uniformes: {"error": "mensaje"}.
 * La app Android lee el campo "error" y se lo muestra al usuario.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 404: el recurso pedido no existe */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RecursoNoEncontradoException ex) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** 400: falló alguna validación (@NotBlank, @NotNull, @Min...) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacion(MethodArgumentNotValidException ex) {
        String detalle = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return construir(HttpStatus.BAD_REQUEST, detalle);
    }

    /** 400: el JSON enviado está mal formado o con tipos incorrectos */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> jsonInvalido(HttpMessageNotReadableException ex) {
        return construir(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no es un JSON válido");
    }

    /** Errores con código propio (ej. 401 credenciales inválidas, 409 correo repetido) */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> estado(ResponseStatusException ex) {
        return construir(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason());
    }

    /** 409: se viola una restricción de la BD (ej. dato único repetido) */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> integridad(DataIntegrityViolationException ex) {
        return construir(HttpStatus.CONFLICT, "El dato ingresado entra en conflicto con otro existente");
    }

    private ResponseEntity<Map<String, String>> construir(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(Map.of("error", String.valueOf(mensaje)));
    }
}
