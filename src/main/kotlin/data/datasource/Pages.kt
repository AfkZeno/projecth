package com.backend.data.datasource

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Pages : IntIdTable("pages") {
    val chapterId = reference(
        name = "chapter_id",
        foreign = Chapters
    )

    val pageNumber = integer("page_number")

    val imageUrl = text("image_url")

    val cloudinaryPublicId = varchar("cloudinary_public_id", 255)
}