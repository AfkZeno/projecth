package com.backend

import com.backend.config.configureAuth
import com.backend.config.configureContentNegotiation
import com.backend.config.configureHTTP
import com.backend.config.configureKoin
import com.backend.config.configureMonitoring
import com.backend.config.configureRoutes
import io.ktor.server.engine.*
import io.ktor.server.application.*
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    embeddedServer(
        factory = Netty,
        port = 8080,
    ) {
        configureContentNegotiation()
        configureMonitoring()
        configureHTTP()
        configureKoin()
        configureAuth()
        configureRoutes()
    }.start(wait = true)
}
