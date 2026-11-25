package com.dirc.novaday.domain.model

data class Objetivo(
    val idObjetivo: Int = 0,
    val titulo: String = "",
    val metaTotal: Int = 1,
    val avanceActual: Int = 0,
    val unidad: String = "",
    val fechaLimite: String? = null,
    val idHabito: Int? = null
) {
    val porcentaje: Int
        get() = if (metaTotal <= 0) 0 else ((avanceActual * 100) / metaTotal).coerceIn(0, 100)
}