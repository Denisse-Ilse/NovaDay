package com.dirc.novaday.domain.repository

import com.dirc.novaday.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    fun obtenerUsuarios(): Flow<List<Usuario>>
    suspend fun registrarUsuario(usuario: Usuario): Result<Unit>
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario?
}