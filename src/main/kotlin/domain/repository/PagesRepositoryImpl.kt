package com.backend.domain.repository

import com.backend.data.datasource.Pages
import com.backend.data.repository.PagesRepository
import com.backend.domain.model.UploadedPage
import com.backend.presentation.response.GlobalResponse
import com.backend.presentation.response.PagesResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

class PagesRepositoryImpl(private val db: R2dbcDatabase): PagesRepository {
    override suspend fun uploadPages(chapterId: Int, result: UploadedPage) : GlobalResponse {
        return suspendTransaction(db) {
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

    override suspend fun getPages(chapterId: Int): List<PagesResponse> {
        return suspendTransaction(db) {
            Pages.selectAll()
                .where { Pages.chapterId eq chapterId }
                .map { row ->
                    PagesResponse(
                        pageNumber = row[Pages.pageNumber],
                        imageKey = row[Pages.imageKey],
                        imageUrl = null,
                        contentType = row[Pages.contentType],
                        width = row[Pages.width],
                        height = row[Pages.height],
                        fileSize = row[Pages.fileSize]
                    )
                }
                .toList()
        }
    }
}