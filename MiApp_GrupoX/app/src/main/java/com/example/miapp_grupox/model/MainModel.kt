
package com.example.miapp_grupox.model

data class MainModel(
    val rol: String = "Operario",
    val pantalla: String = "inicio"
)

data class ResumenSupervisor(
    val totalLineas: Int = 0,
    val normales: Int = 0,
    val advertencias: Int = 0,
    val criticas: Int = 0,
    val totalFlushing: Int = 0
)