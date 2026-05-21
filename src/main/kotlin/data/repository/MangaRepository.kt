package com.backend.data.repository

import com.backend.domain.model.Manga

interface MangaRepository {
    suspend fun createManga(manga: Manga): Int
}