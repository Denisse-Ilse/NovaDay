package com.dirc.novaday.data.di

import com.dirc.novaday.data.repository.HabitoRepositoryImp
import com.dirc.novaday.data.repository.RegistroRepositoryImp
import com.dirc.novaday.data.repository.UsuarioRepositoryImpl
import com.dirc.novaday.domain.repository.HabitoRepository
import com.dirc.novaday.domain.repository.RegistroRepository
import com.dirc.novaday.domain.repository.UsuarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.dirc.novaday.data.repository.ObjetivoRepositoryImp
import com.dirc.novaday.domain.repository.ObjetivoRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHabitoRepository(
        habitoRepositoryImp: HabitoRepositoryImp
    ): HabitoRepository

    @Binds
    @Singleton
    abstract fun bindRegistroRepository(
        registroRepositoryImp: RegistroRepositoryImp
    ): RegistroRepository

    @Binds
    @Singleton
    abstract fun bindUsuarioRepository(
        impl: UsuarioRepositoryImpl
    ): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindObjetivoRepository(
        impl: ObjetivoRepositoryImp
    ): ObjetivoRepository
}