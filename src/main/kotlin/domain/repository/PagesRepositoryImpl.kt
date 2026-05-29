package com.backend.domain.repository

import com.backend.data.datasource.Pages
import com.backend.data.repository.PagesRepository
import com.backend.domain.model.UploadedPage
import com.backend.presentation.response.GlobalResponse
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

class PagesRepositoryImpl(private val db: R2dbcDatabase): PagesRepository {
    override suspend fun uploadPages(chapterId: Int, result: UploadedPage) : GlobalResponse {
        return suspendTransaction {
            Pages.insert {
                it[Pages.chapterId] = chapterId
                it[Pages.pageNumber] = result.pageNumber
                it[Pages.contentType] = result.contentType
                it[Pages.fileSize] = result.fileSize
                it[Pages.imageKey] = result.imageKey
            }[Pages.id].value
            GlobalResponse(
                "Paginas subidas correctamente"
            )
        }
    }
}