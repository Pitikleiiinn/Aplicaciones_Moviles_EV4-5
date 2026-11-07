
package com.example.miapp_grupox.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registros_flushing")
data class FlusingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val granja: String,
    val galpon: String,
    val linea: String,
    val responsable: String,
    val motivo: String,
    val observaciones: String,
    val fecha: String
)