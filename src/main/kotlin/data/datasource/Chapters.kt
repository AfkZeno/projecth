package com.backend.data.datasource

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import org.jetbrains.exposed.v1.javatime.timestamp

object Chapters : IntIdTable("chapters") {
    val mangaId = reference(
        name = "manga_id",
        foreign = Mangas
    )
    val title = varchar("title", 255).nullable()
    val chapterNumber = decimal("chapter_number", 5, 2)
    val images = text("images")
    val views = long("views").default(0)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    init {
        uniqueIndex(mangaId,chapterNumber)
    }
}