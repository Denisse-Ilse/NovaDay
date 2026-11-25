package com.dirc.novaday.domain.repository

import com.dirc.novaday.domain.model.Habito
import kotlinx.coroutines.flow.Flow

interface HabitoRepository {

    fun obtenerHabitos(): Flow<List<Habito>>

    fun obtenerHabitosActivos(): Flow<List<Habito>>

    fun buscarHabitos(query: String): Flow<List<Habito>>

    fun getHabito(id: Int): Flow<Habito?>

    suspend fun insertarHabito(habito: Habito)

    suspend fun actualizarHabito(habito: Habito)

    suspend fun eliminarHabito(habito: Habito)
}