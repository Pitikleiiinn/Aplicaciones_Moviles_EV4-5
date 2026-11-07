package com.example.miapp_grupox.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlusingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(registro: FlusingEntity)

    @Query("SELECT * FROM registros_flushing ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<FlusingEntity>>

    // Modifica un registro existente (se identifica por su id)
    @Update
    suspend fun actualizar(registro: FlusingEntity)

    @Query("DELETE FROM registros_flushing WHERE id = :id")
    suspend fun eliminar(id: Int)

    // Sincronización con ms-flushing: inserta/reemplaza todos los registros recibidos...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(registros: List<FlusingEntity>)

    // ...y borra los que ya no existen en el servidor
    @Query("DELETE FROM registros_flushing WHERE id NOT IN (:ids)")
    suspend fun borrarFueraDe(ids: List<Int>)

    @Query("SELECT COUNT(*) FROM registros_flushing")
    suspend fun contar(): Int
}