package com.dirc.novaday.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "registro",
    foreignKeys = [
        ForeignKey(
            entity = HabitoEntity::class,
            parentColumns = ["idHabito"],
            childColumns = ["idHabito"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idHabito"]),
        Index(value = ["idHabito", "fecha"], unique = true)
    ]
)
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true)
    val idRegistro: Int = 0,
    val idHabito: Int = 0,
    val fecha: String = "",
    val hora: String = "",
    val completado: Boolean = false
) {
    init {
        require(fecha.isNotBlank()) { "La fecha del registro no puede estar vacía" }
    }
}