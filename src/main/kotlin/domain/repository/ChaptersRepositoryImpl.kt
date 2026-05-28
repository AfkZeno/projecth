package com.backend.domain.repository

import com.backend.data.datasource.Chapters
import com.backend.data.datasource.Mangas
import com.backend.data.repository.ChaptersRepository
import com.backend.domain.model.Chapter
import com.backend.presentation.response.ChaptersResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

class ChaptersRepositoryImpl(private val db: R2dbcDatabase) : ChaptersRepository {
    override suspend fun createChapter(chapter: Chapter, mangaId: Int): Int {
        return suspendTransaction(db) {
            Chapters.insert {
                it[Chapters.mangaId] = EntityID(mangaId, Mangas)
                it[title] = chapter.title
                it[chapterNumber] = chapter.chapterNumber.toBigDecimal()
                it[views] = chapter.views
                it[createdAt] = chapter.createdAt
            }[Chapters.id].value
        }
    }

    override suspend fun verifyDup(chapterNumber: Double, mangaId: Int): Boolean {
        return suspendTransaction {
            val exists = Chapters.selectAll()
                .where {
                    (Chapters.mangaId eq mangaId) and
                            (Chapters.chapterNumber eq chapterNumber.toBigDecimal())
                }.empty().not()
            exists
        }
    }

    override suspend fun getChapters(mangaId: Int): List<ChaptersResponse> {
        return suspendTransaction {
            Chapters.selectAll()
                .where { Chapters.mangaId eq mangaId }
                .map {
                    ChaptersResponse(
                        id = it[Chapters.id].value,
                        title = it[Chapters.title] ?: "",
                        chapterNumber = it[Chapters.chapterNumber].toDouble(),
                        views = it[Chapters.views],
                        createdAt = it[Chapters.createdAt].toString()
                    )
                }
                .toList()
        }
    }
}