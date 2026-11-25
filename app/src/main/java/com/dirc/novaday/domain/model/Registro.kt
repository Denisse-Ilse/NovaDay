package com.dirc.novaday.domain.model

data class Registro(
    val idRegistro: Int = 0,
    val idHabito: Int = 0,
    val fecha: String = "",
    val hora: String = "",
    val completado: Boolean = false
)