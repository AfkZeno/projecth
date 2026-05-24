package com.backend.data.repository

import com.backend.domain.model.Chapter

interface ChaptersRepository {
    suspend fun createChapter(chapter: Chapter, mangaId: Int) : Int
    suspend fun verifyDup(chapterNumber: Double, mangaId: Int) : Boolean
}