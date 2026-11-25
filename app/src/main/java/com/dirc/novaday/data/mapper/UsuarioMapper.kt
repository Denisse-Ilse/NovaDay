package com.dirc.novaday.data.mapper

import com.dirc.novaday.data.database.entity.UsuarioEntity
import com.dirc.novaday.domain.model.Usuario

fun UsuarioEntity.toDomain(): Usuario {
    return Usuario(
        idUsuario = idUsuario,
        nombre = nombre,
        correo = correo,
        contrasena = contrasena
    )
}

fun Usuario.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        idUsuario = idUsuario,
        nombre = nombre.trim(),
        correo = correo.trim(),
        contrasena = contrasena
    )
}