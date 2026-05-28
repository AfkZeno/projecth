package com.backend.presentation.routes

import com.backend.config.logger
import com.backend.domain.service.MangaService
import com.backend.infrastructure.backblaze.BackBlazeService
import com.backend.presentation.request.CreateMangaRequestWithImages
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
    val backblaze by inject<BackBlazeService>()



    routing {

        get("/health") {
            call.respond(HealthResponse("xd"))
        }
        // ==================== SUBIDA DE IMÁGENES ====================
        post("/upload/image") {
            println("Uploading image...")
            val multipart = call.receiveMultipart()
            val folder = call.request.queryParameters["folder"] ?: "mangas/covers"

            val result = backblaze.uploadImage(multipart, folder)
            println("temp url = ${result.url}, key = ${result.storageKey}")
            call.respond(HttpStatusCode.Created, result)
        }

        post("/upload/images"){
            val multipart = call.receiveMultipart()
            val folder = call.request.queryParameters["folder"] ?: "mangas/pages"

            val results = backblaze.uploadImages(multipart, folder)
            call.respond(results)
        }

        post("/new/manga") {
            val request = call.receive<CreateMangaRequestWithImages>()
            println("request: $request")

            logger.info("cover = ${request.coverKey}, banner = ${request.bannerKey}")
            val id = service.createManga(request)
            logger.info("new manga id: $id")
            call.respond(HttpStatusCode.Created, GlobalResponse("Manga creado exitosamente, ID: $id"))
        }
        get("/mangas") {
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

            val mangas = service.getAllMangas(limit, offset)
            val finalMangas = mangas.map { manga ->
                manga.copy(
                    coverImageUrl = manga.coverImageKey?.let {
                        backblaze.generatePresignedUrl(it, expiresInMinutes = 60)
                    },
                    bannerImageUrl = manga.bannerImageKey?.let {
                        backblaze.generatePresignedUrl(it, expiresInMinutes = 60)
                    }
                )
            }

            call.respond(HttpStatusCode.OK, finalMangas)
        }
        get("/manga/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val manga = service.getMangaById(id)

            val enrichedManga = manga?.copy(
                coverImageUrl = manga.coverImageKey?.let {
                    backblaze.generatePresignedUrl(it, 120)
                },
                bannerImageUrl = manga.bannerImageKey?.let {
                    backblaze.generatePresignedUrl(it, 120)
                }
            )

            if (enrichedManga != null) {
                call.respond(enrichedManga)
            } else {
                call.respond(HttpStatusCode.NotFound, "Manga no encontrado")
            }
        }
    }
}