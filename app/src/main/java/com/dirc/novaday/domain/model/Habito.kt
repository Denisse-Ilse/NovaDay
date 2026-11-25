package com.dirc.novaday.domain.model

data class Habito(
    val idHabito: Int = 0,
    val nombre: String = "",
    val metaCantidad: Int = 1,
    val metaUnidad: String = "",
    val diasSemana: String = "1111111",
    val activo: Boolean = true,
    val fechaCreacion: String = ""
)