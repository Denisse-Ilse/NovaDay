package com.dirc.novaday.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dirc.novaday.data.database.entity.RegistroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRegistro(registro: RegistroEntity)

    @Update
    suspend fun actualizarRegistro(registro: RegistroEntity)

    @Delete
    suspend fun eliminarRegistro(registro: RegistroEntity)

    @Query("SELECT * FROM registro WHERE fecha = :fecha")
    fun obtenerRegistrosPorFecha(fecha: String): Flow<List<RegistroEntity>>

    @Query("SELECT * FROM registro WHERE idHabito = :idHabito ORDER BY fecha DESC")
    fun obtenerRegistrosPorHabito(idHabito: Int): Flow<List<RegistroEntity>>

    @Query("SELECT * FROM registro WHERE idHabito = :idHabito AND fecha = :fecha LIMIT 1")
    suspend fun obtenerRegistroUnico(idHabito: Int, fecha: String): RegistroEntity?
}