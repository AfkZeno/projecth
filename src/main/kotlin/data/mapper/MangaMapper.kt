package com.backend.data.mapper

import com.backend.domain.model.Manga
import com.backend.presentation.request.CreateMangaRequest
import com.backend.presentation.response.MangaResponse
import com.backend.util.slugify

object MangaMapper {
    fun toDomain(request: CreateMangaRequest, coverImageUrl: String? = null, publicId: String? = null): Manga {
        return Manga(
            title = request.title,
            slug = request.title.slugify(),
            alternativeTitles = request.alternativeTitles,
            description = request.description,
            author = request.author,
            artist = request.artist,
            coverImageUrl = coverImageUrl,
            status = request.status,
            type = request.type,
            tags = request.tags,
            genres = request.genres,
            coverImagePublicId = publicId
        )
    }
    fun toResponse(manga: Manga): MangaResponse {
        return MangaResponse(
            id = manga.id ?: 0,
            title = manga.title,
            slug = manga.slug,
            alternativeTitles = manga.alternativeTitles,
            description = manga.description,
            author = manga.author,
            artist = manga.artist,
            coverImageUrl = manga.coverImageUrl,
            bannerImageUrl = manga.bannerImageUrl,
            status = manga.status,
            type = manga.type,
            tags = manga.tags,
            genres = manga.genres,
            rating = manga.rating,
            views = manga.views,
            createdAt = manga.createdAt,
            updatedAt = manga.updatedAt,

        )
    }
}