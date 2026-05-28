package com.backend.domain.service

import com.backend.data.mapper.ChaptersMapper.toDomain
import com.backend.data.repository.ChaptersRepository
import com.backend.presentation.request.CreateChapterRequest
import com.backend.presentation.response.ChaptersResponse


class ChapterService (private val repository: ChaptersRepository) {
    suspend fun createChapter(request: CreateChapterRequest, mangaId: Int) : Int {
        println("request: $request")
        return  repository.createChapter(request.toDomain(), mangaId)
    }
    suspend fun verifyDup(chapterNumber: Double, mangaId: Int): Boolean {
        return repository.verifyDup(chapterNumber, mangaId)
    }
    suspend fun getChapters(mangaId: Int): List<ChaptersResponse>{
        println("searching for chapters to manga id: $mangaId")
        return repository.getChapters(mangaId)
    }
}