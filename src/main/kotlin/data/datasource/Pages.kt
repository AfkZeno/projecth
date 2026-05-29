package com.backend.data.datasource

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import org.jetbrains.exposed.v1.javatime.timestamp

object Pages : IntIdTable("pages") {
    val chapterId = reference(
        name = "chapter_id",
        foreign = Chapters,
        onDelete = ReferenceOption.CASCADE
    )

    val pageNumber = integer("page_number")
    val imageKey = varchar("image_key", 500)

    val contentType = varchar("content_type", 50).nullable()

    val width = integer("width").nullable()
    val height = integer("height").nullable()

    val fileSize = long("file_size").nullable()

    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    init {
        uniqueIndex(chapterId, pageNumber)
    }

}