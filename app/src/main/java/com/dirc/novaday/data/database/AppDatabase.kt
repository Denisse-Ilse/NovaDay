package com.dirc.novaday.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dirc.novaday.data.database.dao.HabitoDao
import com.dirc.novaday.data.database.dao.ObjetivoDao
import com.dirc.novaday.data.database.dao.RegistroDao
import com.dirc.novaday.data.database.dao.UsuarioDao
import com.dirc.novaday.data.database.entity.HabitoEntity
import com.dirc.novaday.data.database.entity.ObjetivoEntity
import com.dirc.novaday.data.database.entity.RegistroEntity
import com.dirc.novaday.data.database.entity.UsuarioEntity

@Database(
    entities = [
        HabitoEntity::class,
        RegistroEntity::class,
        UsuarioEntity::class,
        ObjetivoEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitoDao(): HabitoDao
    abstract fun registroDao(): RegistroDao
    abstract fun ususrioDao(): UsuarioDao
    abstract fun objetivoDao(): ObjetivoDao
}