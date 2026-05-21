package com.backend.di

import com.backend.data.repository.UserRepositoryImpl
import com.backend.domain.repository.UserRepository
import com.backend.domain.service.UserService
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.koin.dsl.module

val KoinModules = module {
    //repo
    single<UserRepository> { UserRepositoryImpl(get()) }
    //services
    single<UserService> { UserService(get()) }
}

val DatabaseModule = module {
    single<R2dbcDatabase>{
        R2dbcDatabase.connect(
            url = System.getenv("DATABASE_URL") ?: throw IllegalStateException("DATABASE_URL no configurada")
        )
    }
}