package com.backend.di

import com.backend.data.repository.ChaptersRepository
import com.backend.data.repository.MangaRepository
import com.backend.domain.repository.ChaptersRepositoryImpl
import com.backend.domain.repository.MangaRepositoryImpl
import com.backend.domain.service.ChapterService
import com.backend.domain.service.MangaService
import com.backend.infrastructure.cloudinary.CloudinaryConfig
import com.backend.infrastructure.cloudinary.CloudinaryService
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
        CloudinaryConfig(
            cloudName = System.getenv("CLOUDINARY_CLOUD_NAME")
                ?: throw IllegalStateException("CLOUDINARY_CLOUD_NAME no configurada"),
            apiKey = System.getenv("CLOUDINARY_API_KEY")
                ?: throw IllegalStateException("CLOUDINARY_API_KEY no configurada"),
            apiSecret = System.getenv("CLOUDINARY_API_SECRET")
                ?: throw IllegalStateException("CLOUDINARY_API_SECRET no configurada")
        )
    }
    single {
        CloudinaryService(get())
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



