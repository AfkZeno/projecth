package com.backend.presentation.routes

import com.backend.domain.service.ChapterService
import com.backend.domain.service.MangaService
import com.backend.presentation.request.CreateChapterRequest
import com.backend.presentation.response.GlobalResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.chaptersRoute(){

    val service by inject<ChapterService>()
    val mangas by inject<MangaService>()

    routing {
        post("/new/chapter"){
            val mangaId = call.parameters["id"]?.toIntOrNull()
            if (mangaId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid manga id")
                return@post
            }
            val exists = mangas.validateExists(mangaId)
            if (!exists) {
                call.respond(HttpStatusCode.BadRequest, GlobalResponse("El manga no existe"))
                return@post
            }
            val request = call.receive<CreateChapterRequest>()
            val dup = service.verifyDup(request.chapterNumber, mangaId)
            if(dup) {
                call.respond(HttpStatusCode.BadRequest, GlobalResponse("El capitulo ya existe"))
                return@post
            }
            try {
                val id = service.createChapter(request,mangaId)
                call.respond(HttpStatusCode.Created, GlobalResponse("Capitulo añadido, id: $id"))
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, GlobalResponse("Error al añadir el capitulo, mensaje: ${e.message}"))
            }
        }
    }
}