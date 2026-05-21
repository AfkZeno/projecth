package com.backend.domain.model

import com.backend.domain.enums.MangaStatus
import com.backend.domain.enums.MangaType

data class Manga(
    val title: String,
    val description: String,
    val author: String,
    val artist: String,
    val status: MangaStatus,
    val type: MangaType,
    val createdAt: String,
    val updatedAt: String,
)
