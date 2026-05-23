package com.backend.config


import com.backend.di.KoinModules
import com.backend.di.databaseModule
import com.backend.di.storageModule
import com.backend.presentation.routes.mangaRoute
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.path
import io.ktor.util.logging.KtorSimpleLogger
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level


val logger = KtorSimpleLogger("Global Logger")

fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json(
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                prettyPrint = false
                encodeDefaults = true
            }
        )
    }
}

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader("MyCustomHeader")
        anyHost()
    }
}
fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            databaseModule,
            storageModule,
            KoinModules,
        )
    }

}
fun Application.configureRoutes() {
    mangaRoute()
}
fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
    }
}
fun Application.configureAuth(){
    install(Authentication){

    }
}