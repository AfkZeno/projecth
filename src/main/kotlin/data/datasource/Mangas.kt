package com.backend.data.datasource

import com.backend.domain.enums.MangaStatus
import com.backend.domain.enums.MangaType
import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import org.jetbrains.exposed.v1.javatime.timestamp
import java.math.BigDecimal

object Mangas : UIntIdTable("mangas") {
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val author = varchar("author", 150).nullable()
    val artist = varchar("artist", 150).nullable()

    val status = enumerationByName<MangaStatus>("status", 20)  // ONGOING, COMPLETED, HIATUS, CANCELLED
    val type = enumerationByName<MangaType>("type", 20)

    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)

}