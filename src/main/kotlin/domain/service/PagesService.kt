package com.backend.domain.service

import com.backend.data.repository.PagesRepository
import com.backend.domain.model.UploadedPage
import com.backend.presentation.response.GlobalResponse
import com.backend.presentation.response.PagesResponse

class PagesService (private val repository: PagesRepository) {
    suspend fun uploadPages(chapterId: Int, result: UploadedPage): GlobalResponse {
        return repository.uploadPages(
            chapterId,
            result
        )
    }
    suspend fun getPages(chapterId: Int): List<PagesResponse> {
        return repository.getPages(chapterId)
    }
}