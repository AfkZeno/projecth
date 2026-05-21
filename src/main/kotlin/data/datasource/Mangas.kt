package com.backend.data.datasource

import com.backend.domain.enums.MangaStatus
import com.backend.domain.enums.MangaType
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import org.jetbrains.exposed.v1.javatime.timestamp
import java.math.BigDecimal


object Mangas : IntIdTable("mangas") {
    val title = varchar("title", 255)
    val slug =  varchar("slug", 255).uniqueIndex()
    val alternativeTitles = text("alternative_titles").nullable()
    val description = text("description").nullable()
    val coverImageUrl = text("cover_image_url").nullable()
    val bannerImageUrl = text("banner_image_url").nullable()
    val author = varchar("author", 255).nullable()
    val artist = varchar("artist", 120).nullable()


    val status = enumerationByName<MangaStatus>("status", 20)
    val type = enumerationByName<MangaType>("type", 20)

    val genres = text("genres")
    val rating = decimal("rating", 3, 2).default(BigDecimal(0.0))
    val tags = text("tags")
    val views = long("views").default(0)
    val followers = long("followers").default(0)


    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}