package com.backend.domain.repository

import com.backend.data.datasource.Mangas
import com.backend.data.repository.MangaRepository
import com.backend.domain.model.Manga
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

class MangaRepositoryImpl(private val db: R2dbcDatabase): MangaRepository {
    override suspend fun createManga(manga: Manga): Manga {
        return suspendTransaction(db) {
            val statement = Mangas.insert {
                it[title] = manga.title
                it[slug] = manga.slug
                it[alternativeTitles] = manga.alternativeTitles?.let { titles -> kotlinx.serialization.json.Json.encodeToString(titles) }
                it[description] = manga.description
                it[author] = manga.author
                it[artist] = manga.artist
                it[coverImageUrl] = manga.coverImageUrl
                it[bannerImageUrl] = manga.bannerImageUrl
                it[status] = manga.status
                it[type] = manga.type
                it[genres] = kotlinx.serialization.json.Json.encodeToString(manga.genres)
                it[rating] = manga.rating.toBigDecimal()
                it[views] = manga.views
                it[followers] = manga.followers
                it[createdAt] = manga.createdAt
                it[updatedAt] = manga.updatedAt
                it[tags] = kotlinx.serialization.json.Json.encodeToString(manga.tags)
            }
            val id = statement[Mangas.id]
            manga.copy(id = id.value)
        }
    }

    override suspend fun findAll(limit: Int, offset: Int): List<Manga> {
        return suspendTransaction(db) {
            Mangas.selectAll()
                .orderBy(Mangas.createdAt, SortOrder.DESC)
                .limit(limit)
                .offset(offset.toLong())
                .map { it.toManga() }
                .toList()
        }
    }

    override suspend fun findById(id: Int): Manga? {
        return suspendTransaction(db) {
            Mangas.selectAll()
                .where { Mangas.id eq id }
                .singleOrNull()
                ?.toManga()
        }
    }

    private fun ResultRow.toManga(): Manga{
        return Manga(
            id = this[Mangas.id].value,
            title = this[Mangas.title],
            slug = this[Mangas.slug],
            alternativeTitles = this[Mangas.alternativeTitles]?.let {
                kotlinx.serialization.json.Json.decodeFromString(it)
            },
            description = this[Mangas.description],
            author = this[Mangas.author],
            artist = this[Mangas.artist],
            coverImageUrl = this[Mangas.coverImageUrl],
            bannerImageUrl = this[Mangas.bannerImageUrl],
            status = this[Mangas.status],
            type = this[Mangas.type],
            genres = this[Mangas.genres].let {
                kotlinx.serialization.json.Json.decodeFromString(it)
            },
            rating = this[Mangas.rating].toDouble(),
            views = this[Mangas.views],
            followers = this[Mangas.followers],
            createdAt = this[Mangas.createdAt],
            updatedAt = this[Mangas.updatedAt],
            tags = this[Mangas.tags].let {
                kotlinx.serialization.json.Json.decodeFromString(it)
            }
        )
    }
}