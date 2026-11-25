package com.dirc.novaday.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "objetivo",
    foreignKeys = [
        ForeignKey(
            entity = HabitoEntity::class,
            parentColumns = ["idHabito"],
            childColumns = ["idHabito"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["idHabito"])]
)
data class ObjetivoEntity(
    @PrimaryKey(autoGenerate = true)
    val idObjetivo: Int = 0,
    val titulo: String = "",
    val metaTotal: Int = 1,
    val avanceActual: Int = 0,
    val unidad: String = "",
    val fechaLimite: String? = null,
    val idHabito: Int? = null
)