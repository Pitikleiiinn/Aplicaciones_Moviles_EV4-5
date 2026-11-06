package cl.ariztia.bebederos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * [UTILIDAD DE PRUEBAS] Los ViewModel usan Dispatchers.Main (hilo de UI), que no existe en pruebas JVM.
 * Esta regla lo reemplaza por un dispatcher de prueba que ejecuta las corrutinas de inmediato.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(private val dispatcher: kotlinx.coroutines.CoroutineDispatcher = UnconfinedTestDispatcher()) : TestWatcher() {
    override fun starting(description: Description) = Dispatchers.setMain(dispatcher)
    override fun finished(description: Description) = Dispatchers.resetMain()
}
