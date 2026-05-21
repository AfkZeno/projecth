package com.backend.data.repository

import com.backend.domain.model.Manga

interface MangaRepository {
    suspend fun createManga(manga: Manga): Manga
    suspend fun findById(id: Int): Manga?
    suspend fun findAll(limit: Int, offset: Int): List<Manga>
}