package com.backend.di

import com.backend.data.repository.ChaptersRepository
import com.backend.data.repository.MangaRepository
import com.backend.domain.repository.ChaptersRepositoryImpl
import com.backend.domain.repository.MangaRepositoryImpl
import com.backend.domain.service.ChapterService
import com.backend.domain.service.MangaService
import com.backend.infrastructure.backblaze.BackBlazeConfig
import com.backend.infrastructure.backblaze.BackBlazeService
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.koin.dsl.module


val databaseModule = module {
    single<R2dbcDatabase>{
        val url = System.getenv("DATABASE_URL") ?: throw IllegalStateException("DATABASE_URL environment variable is not set!")
        println("Intentando conectar a: $url")
        val option = ConnectionFactoryOptions.parse(url)
        println(ConnectionFactories.supports(option))
        R2dbcDatabase.connect(url)
    }
}

val storageModule = module {
    single {
        BackBlazeConfig(
            bucketName = System.getenv("B2_BUCKET_NAME")
                ?: throw IllegalStateException("B2_BUCKET_NAME no configurada"),

            keyId = System.getenv("B2_KEY_ID")
                ?: throw IllegalStateException("B2_KEY_ID no configurada"),

            applicationKey = System.getenv("B2_APPLICATION_KEY")
                ?: throw IllegalStateException("B2_APPLICATION_KEY no configurada")
        )
    }
    single {
        BackBlazeService(get())
    }
}


val KoinModules = module {
    includes(databaseModule,storageModule)
    //repo
    single<MangaRepository> { MangaRepositoryImpl(get()) }
    single<ChaptersRepository> { ChaptersRepositoryImpl(get()) }
    //service
    single { MangaService(get()) }
    single { ChapterService(get()) }
}



