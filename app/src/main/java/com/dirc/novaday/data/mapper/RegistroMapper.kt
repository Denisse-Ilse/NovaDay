package com.dirc.novaday.data.mapper

import com.dirc.novaday.data.database.entity.RegistroEntity
import com.dirc.novaday.domain.model.Registro

fun RegistroEntity.toDomain(): Registro = Registro(
    idRegistro = idRegistro,
    idHabito = idHabito,
    fecha = fecha,
    hora = hora,
    completado = completado
)

fun Registro.toEntity(): RegistroEntity = RegistroEntity(
    idRegistro = idRegistro,
    idHabito = idHabito,
    fecha = fecha,
    hora = hora,
    completado = completado
)