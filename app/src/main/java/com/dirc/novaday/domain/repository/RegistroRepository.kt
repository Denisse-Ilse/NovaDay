package com.dirc.novaday.domain.repository

import com.dirc.novaday.domain.model.Registro
import kotlinx.coroutines.flow.Flow

interface RegistroRepository {

    fun obtenerRegistrosPorFecha(fecha: String): Flow<List<Registro>>

    fun obtenerRegistrosPorHabito(idHabito: Int): Flow<List<Registro>>

    suspend fun obtenerRegistroUnico(idHabito: Int, fecha: String): Registro?

    suspend fun insertarRegistro(registro: Registro)

    suspend fun actualizarRegistro(registro: Registro)

    suspend fun eliminarRegistro(registro: Registro)
}