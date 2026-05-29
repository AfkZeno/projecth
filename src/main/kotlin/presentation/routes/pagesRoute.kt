package com.backend.presentation.routes

import com.backend.domain.service.PagesService
import com.backend.infrastructure.backblaze.BackBlazeService
import com.backend.presentation.response.GlobalResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.pagesRoute(){

    val backBlazeService by inject<BackBlazeService>()
    val service by inject<PagesService>()

    routing {
        post("/upload/pages/{chapterId}") {

            val chapterId = call.parameters["chapterId"]?.toIntOrNull()
            println("chapterId: $chapterId")
            if (chapterId == null) {
                call.respond(HttpStatusCode.BadRequest, GlobalResponse("ID Invalido"))
                return@post
            }

            try {
                val uploadedPages = backBlazeService.uploadChapterPages(
                    chapterId = chapterId,
                    multipart = call.receiveMultipart()
                )
                val response = uploadedPages.forEach { page ->
                    service.uploadPages(chapterId, page)
                }
                call.respond(HttpStatusCode.OK, response)
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, GlobalResponse("Error al subir las paginas (catch), mensaje: ${e.localizedMessage}"))
            }
        }
    }
}