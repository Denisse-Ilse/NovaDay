package com.dirc.novaday.data.di

import android.content.Context
import androidx.room.Room
import com.dirc.novaday.data.database.AppDatabase
import com.dirc.novaday.data.database.dao.HabitoDao
import com.dirc.novaday.data.database.dao.RegistroDao
import com.dirc.novaday.data.database.dao.UsuarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.dirc.novaday.data.database.dao.ObjetivoDao

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun getInstance(@ApplicationContext context: Context): AppDatabase {
        return synchronized(this) {
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "novaday.db"
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }
    }

    @Provides
    fun provideHabitoDao(database: AppDatabase): HabitoDao {
        return database.habitoDao()
    }

    @Provides
    fun provideRegistroDao(database: AppDatabase): RegistroDao {
        return database.registroDao()
    }

    @Provides
    fun provideUsuarioDao(database: AppDatabase): UsuarioDao {
        return database.ususrioDao()
    }

    @Provides
    fun provideObjetivoDao(database: AppDatabase): ObjetivoDao {
        return database.objetivoDao()
    }

}