package com.backend.presentation.response

import com.backend.domain.enums.MangaStatus
import com.backend.domain.enums.MangaType
import kotlinx.serialization.Serializable


@Serializable
data class MangaResponse(
    val id: Int,
    val title: String,
    val slug: String,
    val alternativeTitles: List<String>?,
    val description: String?,
    val author: String?,
    val artist: String?,
    val status: MangaStatus,
    val type: MangaType,
    val tags: List<String>,
    val genres: List<String>,
    val rating: Double,
    val followers: Long,
    val views: Long,
    val createdAt: String,
    val updatedAt: String,
    val coverImageKey: String?,
    val bannerImageKey: String?,
    val coverImageUrl: String?,
    val bannerImageUrl: String?
)
