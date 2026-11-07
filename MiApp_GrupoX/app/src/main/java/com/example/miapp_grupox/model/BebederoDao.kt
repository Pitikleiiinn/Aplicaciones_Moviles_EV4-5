
package com.example.miapp_grupox.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BebederoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(linea: LineBebedero)

    @Query("SELECT * FROM lineas_bebedero ORDER BY granja, galpon, linea")
    fun obtenerTodas(): Flow<List<LineBebedero>>

    // Sincronización con ms-temperaturas: inserta/reemplaza todas las líneas recibidas...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarTodas(lineas: List<LineBebedero>)

    // ...y borra las que ya no existen en el servidor
    @Query("DELETE FROM lineas_bebedero WHERE id NOT IN (:ids)")
    suspend fun borrarFueraDe(ids: List<Int>)

    @Query("SELECT COUNT(*) FROM lineas_bebedero")
    suspend fun contar(): Int

    @Query("SELECT * FROM lineas_bebedero WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): LineBebedero?
}