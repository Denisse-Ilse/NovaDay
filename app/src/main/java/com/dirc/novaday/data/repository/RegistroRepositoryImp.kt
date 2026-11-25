package com.dirc.novaday.data.repository

import com.dirc.novaday.data.database.dao.RegistroDao
import com.dirc.novaday.data.mapper.toDomain
import com.dirc.novaday.data.mapper.toEntity
import com.dirc.novaday.domain.model.Registro
import com.dirc.novaday.domain.repository.RegistroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistroRepositoryImp @Inject constructor(
    private val registroDao: RegistroDao
) : RegistroRepository {

    override fun obtenerRegistrosPorFecha(fecha: String): Flow<List<Registro>> {
        return registroDao.obtenerRegistrosPorFecha(fecha).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun obtenerRegistrosPorHabito(idHabito: Int): Flow<List<Registro>> {
        return registroDao.obtenerRegistrosPorHabito(idHabito).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun obtenerRegistroUnico(idHabito: Int, fecha: String): Registro? {
        return registroDao.obtenerRegistroUnico(idHabito, fecha)?.toDomain()
    }

    override suspend fun insertarRegistro(registro: Registro) {
        registroDao.insertarRegistro(registro.toEntity())
    }

    override suspend fun actualizarRegistro(registro: Registro) {
        registroDao.actualizarRegistro(registro.toEntity())
    }

    override suspend fun eliminarRegistro(registro: Registro) {
        registroDao.eliminarRegistro(registro.toEntity())
    }
}