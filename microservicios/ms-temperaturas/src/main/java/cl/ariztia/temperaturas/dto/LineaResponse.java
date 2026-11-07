package cl.ariztia.temperaturas.dto;

/**
 * [DTO SALIDA] Línea con su última temperatura. Los nombres de campo coinciden con la clase
 * LineBebedero de la app (id, granja, galpon, linea, temperatura, fecha) para que Gson la lea directo.
 */
public record LineaResponse(Long id, String granja, String galpon, String linea,
                            Double temperatura, String estado, String fecha) {
}
