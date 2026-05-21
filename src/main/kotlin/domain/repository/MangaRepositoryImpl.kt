package com.backend.domain.repository

import com.backend.data.repository.MangaRepository
import com.backend.domain.model.Manga
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

class MangaRepositoryImpl(private val db: R2dbcDatabase): MangaRepository {
    override suspend fun createManga(manga: Manga): Int {
        TODO("Not yet implemented")
    }
}