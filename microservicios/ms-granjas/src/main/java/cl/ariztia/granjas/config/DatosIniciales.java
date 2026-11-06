package cl.ariztia.granjas.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.ariztia.granjas.model.Galpon;
import cl.ariztia.granjas.model.Granja;
import cl.ariztia.granjas.model.LineaBebedero;
import cl.ariztia.granjas.repository.GranjaRepository;

/**
 * [DATOS DE PRUEBA] Datos ficticios (el caso lo exige): 2 granjas x 2 galpones x 2 líneas = 8 líneas (ids 1..8).
 * Solo se cargan si la BD está vacía.
 */
@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner cargarGranjas(GranjaRepository repo) {
        return args -> crearSiVacio(repo);
    }

    void crearSiVacio(GranjaRepository repo) {
        if (repo.count() > 0) return;
        repo.save(granja("Los Aromos", "Melipilla, Región Metropolitana", -33.6889, -71.2153));
        repo.save(granja("El Roble", "Rancagua, Región de O'Higgins", -34.1708, -70.7444));
    }

    private Granja granja(String nombre, String ubicacion, double lat, double lon) {
        Granja g = new Granja();
        g.setNombre(nombre);
        g.setUbicacion(ubicacion);
        g.setLatitud(lat);
        g.setLongitud(lon);
        for (int i = 1; i <= 2; i++) {
            Galpon galpon = new Galpon();
            galpon.setNombre("Galpón " + i);
            galpon.setGranja(g);
            for (String n : new String[] {"Línea A", "Línea B"}) {
                LineaBebedero l = new LineaBebedero();
                l.setNombre(n);
                l.setGalpon(galpon);
                galpon.getLineas().add(l);
            }
            g.getGalpones().add(galpon);
        }
        return g;
    }
}
