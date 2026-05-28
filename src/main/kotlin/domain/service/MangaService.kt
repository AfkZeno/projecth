package com.backend.domain.service

import com.backend.data.mapper.MangaMapper
import com.backend.data.mapper.MangaMapper.toDomain
import com.backend.data.repository.MangaRepository
import com.backend.presentation.request.CreateMangaRequestWithImages
import com.backend.presentation.response.MangaResponse
import org.koin.core.component.KoinComponent

class MangaService(private val repository: MangaRepository) : KoinComponent {

    suspend fun createManga(request: CreateMangaRequestWithImages): Int {
        val domain = toDomain(request)
        println("Request converted to domain, final: $domain")
        return repository.createManga(domain)
    }
    suspend fun getMangaById(id: Int): MangaResponse? {
        val manga = repository.findById(id) ?: return null
        return MangaMapper.toResponse(manga)
    }
    suspend fun getAllMangas(limit: Int = 20, offset: Int = 0): List<MangaResponse> {
        return repository.findAll(limit, offset).map { MangaMapper.toResponse(it) }
    }

    suspend fun validateExists(id: Int): Boolean {
        val exists = repository.findAndValidate(id)
        return exists
    }
}