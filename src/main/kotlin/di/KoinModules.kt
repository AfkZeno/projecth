package com.backend.di

import com.backend.data.repository.MangaRepository
import com.backend.database.DbFactory
import com.backend.domain.repository.MangaRepositoryImpl
import com.backend.domain.service.MangaService
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.koin.dsl.module

val KoinModules = module {
    //repo
    single<MangaRepository> { MangaRepositoryImpl(get()) }
    //service
    single { MangaService(get()) }
}

val DatabaseModule = module {
    single<R2dbcDatabase>{
        val url = System.getenv("DATABASE_URL") ?: throw IllegalStateException("DATABASE_URL environment variable is not set!")
        println("Intentando conectar a: $url")
        val option = ConnectionFactoryOptions.parse(url)
        println(ConnectionFactories.supports(option))
        R2dbcDatabase.connect(url)
    }
}