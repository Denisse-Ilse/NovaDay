package com.dirc.novaday.domain.repository

import com.dirc.novaday.domain.model.Objetivo
import kotlinx.coroutines.flow.Flow

interface ObjetivoRepository {
    fun obtenerObjetivos(): Flow<List<Objetivo>>
    suspend fun insertarObjetivo(objetivo: Objetivo)
    suspend fun eliminarObjetivo(objetivo: Objetivo)
}