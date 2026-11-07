package cl.ariztia.temperaturas.model;

/** [MODELO] Estado de una línea. La etiqueta es el texto que usa la app ("Normal", "Advertencia", "Crítico"). */
public enum EstadoLinea {
    NORMAL("Normal"),
    ADVERTENCIA("Advertencia"),
    CRITICO("Crítico");

    private final String etiqueta;

    EstadoLinea(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
