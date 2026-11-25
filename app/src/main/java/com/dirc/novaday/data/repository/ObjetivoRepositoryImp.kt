package com.dirc.novaday.data.repository

import com.dirc.novaday.data.database.dao.ObjetivoDao
import com.dirc.novaday.data.mapper.toDomain
import com.dirc.novaday.data.mapper.toEntity
import com.dirc.novaday.domain.model.Objetivo
import com.dirc.novaday.domain.repository.ObjetivoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObjetivoRepositoryImp @Inject constructor(
    private val objetivoDao: ObjetivoDao
) : ObjetivoRepository {

    override fun obtenerObjetivos(): Flow<List<Objetivo>> {
        return objetivoDao.obtenerObjetivos().map { lista -> lista.map { it.toDomain() } }
    }

    override suspend fun insertarObjetivo(objetivo: Objetivo) {
        objetivoDao.insertarObjetivo(objetivo.toEntity())
    }

    override suspend fun eliminarObjetivo(objetivo: Objetivo) {
        objetivoDao.eliminarObjetivo(objetivo.toEntity())
    }
}