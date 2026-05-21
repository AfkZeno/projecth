package com.backend.domain.service

import com.backend.data.mapper.MangaMapper
import com.backend.data.repository.MangaRepository
import com.backend.infrastructure.cloudinary.CloudinaryService
import com.backend.presentation.request.CreateMangaRequest
import com.backend.presentation.response.MangaResponse
import com.backend.util.slugify
import io.ktor.http.content.PartData
import org.koin.core.component.KoinComponent

class MangaService(private val repository: MangaRepository, private val cloudinary: CloudinaryService) : KoinComponent {
    suspend fun createManga(
        request: CreateMangaRequest,
        coverFile: PartData.FileItem? = null
    ): MangaResponse {
        validateCreateMangaRequest(request)
        var coverImageUrl: String? = null
        if (coverFile != null) {
            val publicId = request.title.slugify()
            coverImageUrl = cloudinary.uploadImage(
                file = coverFile,
                folder = "mangas/covers",
                publicId = publicId
            )
            println("✅ Imagen subida: $coverImageUrl")
        }
        val mangaDomain = MangaMapper.toDomain(request, coverImageUrl)
        val savedManga = repository.createManga(mangaDomain)
        return MangaMapper.toResponse(savedManga)
    }


    suspend fun getMangaById(id: Int): MangaResponse? {
        val manga = repository.findById(id) ?: return null
        return MangaMapper.toResponse(manga)
    }
    suspend fun getAllMangas(limit: Int = 20, offset: Int = 0): List<MangaResponse> {
        return repository.findAll(limit, offset).map { MangaMapper.toResponse(it) }
    }

    private fun validateCreateMangaRequest(request: CreateMangaRequest) {
        if (request.title.isBlank()) {
            throw IllegalArgumentException("El título es obligatorio")
        }
        if (request.title.length < 3) {
            throw IllegalArgumentException("El título debe tener al menos 3 caracteres")
        }
        // optionally add more validations
    }
}