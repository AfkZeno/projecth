package com.backend.presentation.routes

import com.backend.domain.service.MangaService
import com.backend.presentation.request.CreateMangaRequest
import com.backend.presentation.response.HealthResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.mangaRoute(){
    val service by inject<MangaService>()

    routing {

        get("/health"){
            call.respond(HealthResponse("xd"))
        }

        post("/new/manga") {
            try {
                val multipart = call.receiveMultipart()
                var request: CreateMangaRequest? = null
                var coverFile: PartData.FileItem? = null

                multipart.forEachPart { part ->
                    when(part) {
                        is PartData.FormItem -> {
                            when (part.name){
                                "title" -> {
                                    request = (request ?: CreateMangaRequest(title = "")).copy(
                                        title = part.value
                                    )
                                }
                                "description" -> {
                                    request = request?.copy(description = part.value)
                                }
                                "author" -> {
                                    request = request?.copy(author = part.value)
                                }
                                "artist" -> {
                                    request = request?.copy(artist = part.value)
                                }
                                "status" -> {
                                    request = request?.copy(status = enumValueOf(part.value))
                                }
                                "type" -> {
                                    request = request?.copy(type = enumValueOf(part.value))
                                }
                                "tags" -> {
                                    val tagsList = part.value.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                    request = request?.copy(tags = tagsList)
                                }
                                "genres" -> {
                                    val genresList = part.value.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                    request = request?.copy(genres = genresList)
                                }
                            }
                        }
                        is PartData.FileItem -> {
                            if(part.name == "cover"){
                                coverFile = part
                            }
                        }
                        else -> part.release()
                    }
                }
                val finalRequest = request ?: throw IllegalArgumentException("No se recibieron datos del manga")
                val response = service.createManga(
                    request = finalRequest,
                    coverFile = coverFile
                )
                call.respond(HttpStatusCode.Created, response)
            }catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al crear el manga"))
            }
        }
        get("/mangas"){
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

            val mangas = service.getAllMangas(limit, offset)
            call.respond(mangas)
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