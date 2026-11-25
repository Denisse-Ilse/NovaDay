package com.dirc.novaday.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dirc.novaday.data.database.entity.HabitoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarHabito(habito: HabitoEntity)

    @Update
    suspend fun actualizarHabito(habito: HabitoEntity)

    @Delete
    suspend fun eliminarHabito(habito: HabitoEntity)

    @Query("SELECT * FROM habito WHERE idHabito = :id")
    fun getHabito(id: Int): Flow<HabitoEntity?>

    @Query("SELECT * FROM habito ORDER BY idHabito DESC")
    fun obtenerHabitos(): Flow<List<HabitoEntity>>

    @Query("SELECT * FROM habito WHERE activo = 1 ORDER BY idHabito DESC")
    fun obtenerHabitosActivos(): Flow<List<HabitoEntity>>

    @Query("SELECT * FROM habito WHERE nombre LIKE '%' || :query || '%' ORDER BY nombre ASC")
    fun buscarHabitos(query: String): Flow<List<HabitoEntity>>
}