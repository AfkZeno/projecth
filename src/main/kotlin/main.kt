package com.backend

import com.backend.config.configureAuth
import com.backend.config.configureContentNegotiation
import com.backend.config.configureHTTP
import com.backend.config.configureKoin
import com.backend.config.configureMonitoring
import com.backend.config.configureRoutes
import com.backend.database.DbFactory
import io.ktor.server.engine.*
import io.ktor.server.application.*
import io.ktor.server.netty.Netty
import kotlinx.coroutines.launch
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.koin.core.component.getScopeName
import org.koin.ktor.ext.get
import java.util.ServiceLoader

fun main(args: Array<String>) {
    embeddedServer(
        factory = Netty,
        port = 8080,
    ) {
    module()
    }.start(wait = true)
}

fun Application.module(){
    configureContentNegotiation()
    configureMonitoring()
    configureHTTP()
    configureAuth()

    configureKoin()
    ServiceLoader.load(
        io.r2dbc.spi.ConnectionFactoryProvider::class.java
    ).forEach {
        println("DRIVER -> ${it.getScopeName()}")
    }
    val db = get<R2dbcDatabase>()
    launch {
        try {
            DbFactory.initDb(db)
        }catch (e: Exception){
            log.error("Error al inicializar la base de datos", e)
        }
    }


    configureRoutes()
}
