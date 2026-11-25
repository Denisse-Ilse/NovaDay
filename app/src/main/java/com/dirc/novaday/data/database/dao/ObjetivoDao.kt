package com.dirc.novaday.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dirc.novaday.data.database.entity.ObjetivoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjetivoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarObjetivo(objetivo: ObjetivoEntity)

    @Delete
    suspend fun eliminarObjetivo(objetivo: ObjetivoEntity)

    @Query("SELECT * FROM objetivo ORDER BY idObjetivo DESC")
    fun obtenerObjetivos(): Flow<List<ObjetivoEntity>>
}