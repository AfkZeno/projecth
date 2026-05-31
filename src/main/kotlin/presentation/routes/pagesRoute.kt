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
                uploadedPages.forEach { page ->
                    service.uploadPages(chapterId, page)
                }
                println("Exito: $uploadedPages")
                call.respond(HttpStatusCode.OK, GlobalResponse("Paginas añadidas exitosamente, resumen: $uploadedPages"))
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, GlobalResponse("Error al subir las paginas (catch), mensaje: ${e.localizedMessage}"))
            }
        }

        get("/pages/{chapterId}"){
            val chapterId = call.parameters["chapterId"]?.toIntOrNull()
            println("chapterId: $chapterId")
            if (chapterId == null) {
                call.respond(HttpStatusCode.BadRequest, GlobalResponse("ID Invalido"))
                return@get
            }
            try {
                val pages = service.getPages(chapterId)
                val finalPages = pages.map { pagesResponse ->
                    pagesResponse.copy(
                        imageUrl = pagesResponse.imageKey?.let {
                            backBlazeService.generatePresignedUrl(it, 120)
                        }
                    )
                }
                call.respond(HttpStatusCode.OK, finalPages)
            }catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
                println("Hubo un error al obtener las paginas, ${e.message}")
            }
        }
    }
}