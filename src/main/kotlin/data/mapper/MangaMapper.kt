package com.backend.data.mapper

import com.backend.domain.model.Manga
import com.backend.presentation.request.CreateMangaRequestWithImages
import com.backend.presentation.response.MangaResponse
import com.backend.util.slugify

object MangaMapper {
    fun toDomain(request: CreateMangaRequestWithImages): Manga {
        return Manga(
            title = request.title,
            slug = request.title.slugify(),
            alternativeTitles = request.alternativeTitles,
            description = request.description,
            author = request.author,
            artist = request.artist,
            status = request.status,
            type = request.type,
            tags = request.tags,
            genres = request.genres,
            coverImageKey = request.coverKey,
            bannerImageKey = request.bannerKey
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
            status = manga.status,
            type = manga.type,
            tags = manga.tags,
            genres = manga.genres,
            rating = manga.rating,
            views = manga.views,
            followers = manga.followers,
            createdAt = manga.createdAt.toString(),
            updatedAt = manga.updatedAt.toString(),
            coverImageKey = manga.coverImageKey,
            bannerImageKey = manga.bannerImageKey
        )
    }
}