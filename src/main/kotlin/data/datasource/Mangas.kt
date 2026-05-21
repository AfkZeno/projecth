package com.backend.data.datasource

import com.backend.domain.enums.MangaStatus
import com.backend.domain.enums.MangaType
import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import org.jetbrains.exposed.v1.javatime.timestamp
import java.math.BigDecimal

object Mangas : UIntIdTable("mangas") {
    val title = varchar("title", 255)
    val alternativeTitles = text("alternative_titles").nullable()  // JSON o separado por comas
    val description = text("description").nullable()
    val author = varchar("author", 150).nullable()
    val artist = varchar("artist", 150).nullable()

    val status = enumerationByName<MangaStatus>("status", 20)  // ONGOING, COMPLETED, HIATUS, CANCELLED
    val type = enumerationByName<MangaType>("type", 20)

    val coverImageUrl = varchar("cover_image_url", 500).nullable()
    val bannerImageUrl = varchar("banner_image_url", 500).nullable()

    val genres = text("genres").nullable()                    // JSON array o tabla intermedia
    val tags = text("tags").nullable()                        // JSON array

    val rating = decimal("rating", 3, 2).default(BigDecimal(0.0))
    val views = long("views").default(0)
    val followers = long("followers").default(0)

    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)

}