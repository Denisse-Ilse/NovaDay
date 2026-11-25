package com.dirc.novaday.data.database.entity;

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habito")
data class HabitoEntity(
    @PrimaryKey(autoGenerate = true)
    val idHabito: Int = 0,
    val nombre: String = "",
    val metaCantidad: Int = 1,
    val metaUnidad: String = "",
    val diasSemana: String = "1111111",
    val activo: Boolean = true,
    val fechaCreacion: String = ""
) {
    init {
        require(nombre.length <= 40) { "El nombre del hábito no puede tener más de 40 caracteres" }
        require(metaCantidad > 0) { "La meta debe ser mayor a 0" }
        require(diasSemana.length == 7) { "Los días de la semana deben tener 7 caracteres" }
    }
}