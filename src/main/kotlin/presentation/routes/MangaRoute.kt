package com.backend.presentation.routes

import com.backend.domain.service.MangaService
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.mangaRoute(){
    val service by inject<MangaService>()

    routing {
        post("/new/manga") {  }
        get("/mangas"){
            call.respondText("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE")
        }
    }
}