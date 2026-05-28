package com.backend.domain.model

import java.time.Instant


data class Chapter(
    val id: Int? = null,
    val title: String?,
    val chapterNumber: Double,
    val views: Long = 0,
    val createdAt: Instant = Instant.now()
)
