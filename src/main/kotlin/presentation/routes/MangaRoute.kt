package com.backend.presentation.routes

import com.backend.config.logger
import com.backend.domain.service.MangaService
import com.backend.infrastructure.cloudinary.CloudinaryService
import com.backend.presentation.request.CreateMangaRequest
import com.backend.presentation.response.GlobalResponse
import com.backend.presentation.response.HealthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.mangaRoute() {
    val service by inject<MangaService>()
    val cloudinary by inject<CloudinaryService>()



    routing {

        get("/health") {
            call.respond(HealthResponse("xd"))
        }

        post("/upload/image") {
            logger.info("Uploading image...")
            val multipart = call.receiveMultipart()

            val result = cloudinary.uploadImage(multipart)
            logger.info("publicId = ${result.publicId}, url = ${result.url}")
            call.respond(result)
        }

        post("/new/manga") {
            val request = call.receive<CreateMangaRequest>()
            logger.info("request: $request")
            val url = call.request.queryParameters["imageUrl"]
            val publicId = call.request.queryParameters["publicId"]

            logger.info("url = $url, publicId = $publicId")
            val id = service.createManga(request, url, publicId)
            logger.info("new manga id: $id")
            call.respond(HttpStatusCode.Created, GlobalResponse("Manga creado exitosamente, ID: $id"))
        }
        get("/mangas") {
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

            val mangas = service.getAllMangas(limit, offset)
            call.respond(HttpStatusCode.OK, mangas)
        }
        get("/manga/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val manga = service.getMangaById(id)

            if (manga != null) {
                call.respond(manga)
            } else {
                call.respond(HttpStatusCode.NotFound, "Manga no encontrado")
            }
        }
    }
}