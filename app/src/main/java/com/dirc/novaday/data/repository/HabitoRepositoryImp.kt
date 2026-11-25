package com.dirc.novaday.data.repository

import com.dirc.novaday.data.database.dao.HabitoDao
import com.dirc.novaday.data.mapper.toDomain
import com.dirc.novaday.data.mapper.toEntity
import com.dirc.novaday.domain.model.Habito
import com.dirc.novaday.domain.repository.HabitoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitoRepositoryImp @Inject constructor(
    private val habitoDao: HabitoDao
) : HabitoRepository {

    override fun obtenerHabitos(): Flow<List<Habito>> {
        return habitoDao.obtenerHabitos().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun obtenerHabitosActivos(): Flow<List<Habito>> {
        return habitoDao.obtenerHabitosActivos().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun buscarHabitos(query: String): Flow<List<Habito>> {
        return habitoDao.buscarHabitos(query).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getHabito(id: Int): Flow<Habito?> {
        return habitoDao.getHabito(id).map { it?.toDomain() }
    }

    override suspend fun insertarHabito(habito: Habito) {
        habitoDao.insertarHabito(habito.toEntity())
    }

    override suspend fun actualizarHabito(habito: Habito) {
        habitoDao.actualizarHabito(habito.toEntity())
    }

    override suspend fun eliminarHabito(habito: Habito) {
        habitoDao.eliminarHabito(habito.toEntity())
    }
}