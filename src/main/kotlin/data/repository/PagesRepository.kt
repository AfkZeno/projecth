package com.backend.data.repository

import com.backend.domain.model.UploadedPage
import com.backend.presentation.response.GlobalResponse
import com.backend.presentation.response.PagesResponse

interface PagesRepository {
    suspend fun uploadPages(
        chapterId: Int,
        result: UploadedPage
    ) : GlobalResponse

    suspend fun getPages(
        chapterId: Int
    ): List<PagesResponse>
}