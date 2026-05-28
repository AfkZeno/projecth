package com.backend.data.repository

import com.backend.domain.model.Chapter
import com.backend.presentation.response.ChaptersResponse

interface ChaptersRepository {
    suspend fun createChapter(chapter: Chapter, mangaId: Int) : Int
    suspend fun verifyDup(chapterNumber: Double, mangaId: Int) : Boolean
    suspend fun getChapters(mangaId: Int): List<ChaptersResponse>
}