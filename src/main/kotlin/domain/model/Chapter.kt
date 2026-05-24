package com.backend.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Chapter(
    val id: Int? = null,
    val title: String?,
    val chapterNumber: Double,
    val views: Long = 0,
    @Contextual val createdAt: Instant = Instant.now()
)
