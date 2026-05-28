package com.backend.domain.model

import com.backend.domain.enums.MangaStatus
import com.backend.domain.enums.MangaType
import java.time.Instant


data class Manga(
    val id: Int? = null,

    val title: String,
    val slug: String,                    // URL amigable (único)
    val alternativeTitles: List<String>? = null,

    val description: String? = null,
    val author: String? = null,
    val artist: String? = null,

    val status: MangaStatus = MangaStatus.ONGOING,
    val type: MangaType = MangaType.MANGA,

    val genres: List<String> = emptyList(),

    val rating: Double = 0.0,
    val views: Long = 0,
    val followers: Long = 0,

    val tags: List<String>,

    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),

    val coverImageKey: String? = null,
    val bannerImageKey: String? = null
)
