package com.backend.data.datasource

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Pages : IntIdTable("pages") {
    val chapterId = reference(
        name = "chapter_id",
        foreign = Chapters
    )

    val pageNumber = integer("page_number")
    val imageKey = varchar("image_key", 500).nullable()   // ← nuevo
}