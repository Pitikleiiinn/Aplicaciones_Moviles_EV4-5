package cl.ariztia.bebederos

import cl.ariztia.bebederos.data.repository.ClimaRepository
import cl.ariztia.bebederos.data.repository.FlushingRepository
import cl.ariztia.bebederos.data.repository.GranjasRepository
import cl.ariztia.bebederos.data.repository.TemperaturasRepository
import cl.ariztia.bebederos.data.repository.UsuariosRepository

/**
 * [INYECCIÓN DE DEPENDENCIAS MANUAL] Crea una sola vez cada repositorio y lo entrega a los ViewModel.
 * (En un proyecto grande se usaría Hilt; aquí se mantiene simple para poder explicarlo en la defensa.)
 */
object AppContainer {
    val usuarios by lazy { UsuariosRepository() }
    val granjas by lazy { GranjasRepository() }
    val temperaturas by lazy { TemperaturasRepository() }
    val flushing by lazy { FlushingRepository() }
    val clima by lazy { ClimaRepository() }
}
