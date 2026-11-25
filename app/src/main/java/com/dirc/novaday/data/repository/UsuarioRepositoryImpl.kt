package com.dirc.novaday.data.repository

import com.dirc.novaday.data.database.dao.UsuarioDao
import com.dirc.novaday.data.mapper.toDomain
import com.dirc.novaday.data.mapper.toEntity
import com.dirc.novaday.domain.model.Usuario
import com.dirc.novaday.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val usuarioDao: UsuarioDao
) : UsuarioRepository {

    override fun obtenerUsuarios(): Flow<List<Usuario>> {
        return usuarioDao.obtenerUsuarios().map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun registrarUsuario(usuario: Usuario): Result<Unit> {
        return try {
            val existente = usuarioDao.buscarPorCorreo(usuario.correo.trim())
            if (existente != null) {
                Result.failure(IllegalStateException("Ese correo ya está registrado"))
            } else {
                usuarioDao.insertarUsuario(usuario.toEntity())
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        return usuarioDao.iniciarSesion(correo.trim(), contrasena)?.toDomain()
    }
}