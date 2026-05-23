package com.backend.domain.service

import com.backend.data.mapper.MangaMapper
import com.backend.data.mapper.MangaMapper.toDomain
import com.backend.data.repository.MangaRepository
import com.backend.presentation.request.CreateMangaRequest
import com.backend.presentation.response.MangaResponse
import io.ktor.util.logging.*
import org.koin.core.component.KoinComponent

class MangaService(private val repository: MangaRepository) : KoinComponent {

    val slogger = KtorSimpleLogger("Manga Service")

    suspend fun createManga(request: CreateMangaRequest, coverImage: String?, publicId: String?): Int {
        slogger.info("request: $request, image: $coverImage, publicId: $publicId")
        val domain = toDomain(request, coverImage, publicId)
        slogger.info("Request converted to domain, final: $domain")
        return repository.createManga(domain)
    }
    suspend fun getMangaById(id: Int): MangaResponse? {
        val manga = repository.findById(id) ?: return null
        return MangaMapper.toResponse(manga)
    }
    suspend fun getAllMangas(limit: Int = 20, offset: Int = 0): List<MangaResponse> {
        return repository.findAll(limit, offset).map { MangaMapper.toResponse(it) }
    }
}