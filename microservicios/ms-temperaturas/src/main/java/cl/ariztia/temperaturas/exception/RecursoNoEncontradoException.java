package cl.ariztia.temperaturas.exception;

/** [EXCEPCIÓN] Se lanza cuando un recurso (por id) no existe. Se traduce a HTTP 404. */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
