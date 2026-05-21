package com.backend.presentation.response

import com.backend.domain.enums.MangaStatus
import com.backend.domain.enums.MangaType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant


@Serializable
data class MangaResponse(
    val id: Int,
    val title: String,
    val slug: String,
    val alternativeTitles: List<String>?,
    val description: String?,
    val author: String?,
    val artist: String?,
    val coverImageUrl: String?,
    val bannerImageUrl: String?,
    val status: MangaStatus,
    val type: MangaType,
    val genres: List<String>,
    val rating: Double,
    val views: Long,
    @Contextual val createdAt: Instant,
    @Contextual val updatedAt: Instant
)
