package com.backend.presentation.routes

import com.backend.data.repository.PagesRepository
import com.backend.domain.service.PagesService
import com.backend.infrastructure.backblaze.BackBlazeService
import com.backend.presentation.response.GlobalResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.pagesRoute(){

    val backBlazeService by inject<BackBlazeService>()
    val service by inject<PagesService>()

    routing {
        post("/upload/pages/{chapterId}") {
            val chapterId = call.parameters["chapterId"]?.toIntOrNull()
                ?: throw BadRequestException("Chapter ID inválido")
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
                call.respond(HttpStatusCode.BadRequest, GlobalResponse(e.message ?: "Error al subir las paginas"))
            }
        }
    }
}