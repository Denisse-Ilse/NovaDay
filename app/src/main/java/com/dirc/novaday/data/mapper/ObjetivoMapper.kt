package com.dirc.novaday.data.mapper

import com.dirc.novaday.data.database.entity.ObjetivoEntity
import com.dirc.novaday.domain.model.Objetivo

fun ObjetivoEntity.toDomain(): Objetivo = Objetivo(
    idObjetivo = idObjetivo,
    titulo = titulo,
    metaTotal = metaTotal,
    avanceActual = avanceActual,
    unidad = unidad,
    fechaLimite = fechaLimite,
    idHabito = idHabito
)

fun Objetivo.toEntity(): ObjetivoEntity = ObjetivoEntity(
    idObjetivo = idObjetivo,
    titulo = titulo,
    metaTotal = metaTotal,
    avanceActual = avanceActual,
    unidad = unidad,
    fechaLimite = fechaLimite,
    idHabito = idHabito
)