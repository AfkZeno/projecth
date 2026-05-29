package com.backend.data.repository

import com.backend.domain.model.UploadedPage
import com.backend.presentation.response.GlobalResponse

interface PagesRepository {
    suspend fun uploadPages(
        chapterId: Int,
        result: UploadedPage
    ) : GlobalResponse
}