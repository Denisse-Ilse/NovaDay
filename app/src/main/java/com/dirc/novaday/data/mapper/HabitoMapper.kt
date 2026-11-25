package com.dirc.novaday.data.mapper

import com.dirc.novaday.data.database.entity.HabitoEntity
import com.dirc.novaday.domain.model.Habito

fun HabitoEntity.toDomain(): Habito = Habito(
    idHabito = idHabito,
    nombre = nombre,
    metaCantidad = metaCantidad,
    metaUnidad = metaUnidad,
    diasSemana = diasSemana,
    activo = activo,
    fechaCreacion = fechaCreacion
)

fun Habito.toEntity(): HabitoEntity = HabitoEntity(
    idHabito = idHabito,
    nombre = nombre,
    metaCantidad = metaCantidad,
    metaUnidad = metaUnidad,
    diasSemana = diasSemana,
    activo = activo,
    fechaCreacion = fechaCreacion
)