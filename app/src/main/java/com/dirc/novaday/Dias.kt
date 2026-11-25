package com.dirc.novaday

val LETRAS_DIAS = listOf("L", "M", "X", "J", "V", "S", "D")
private val NOMBRES_DIAS = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

fun formatearDias(diasSemana: String): String {
    val esMascara = diasSemana.length == 7 && diasSemana.all { it == '0' || it == '1' }
    if (!esMascara) return diasSemana

    val activos = diasSemana.indices.filter { diasSemana[it] == '1' }
    return when (activos) {
        emptyList<Int>() -> "Sin días"
        listOf(0, 1, 2, 3, 4, 5, 6) -> "Todos los días"
        listOf(0, 1, 2, 3, 4) -> "De lunes a viernes"
        listOf(5, 6) -> "Fines de semana"
        else -> activos.joinToString(", ") { NOMBRES_DIAS[it] }
    }
}