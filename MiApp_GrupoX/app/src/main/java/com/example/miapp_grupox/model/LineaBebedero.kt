
package com.example.miapp_grupox.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lineas_bebedero")
data class LineBebedero(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val granja: String,
    val galpon: String,
    val linea: String,
    val temperatura: Double,
    val fecha: String
) {
    val estado: String
        get() = when {
            temperatura >= 30.0 -> "Crítico"
            temperatura >= 27.0 -> "Advertencia"
            else -> "Normal"
        }
}