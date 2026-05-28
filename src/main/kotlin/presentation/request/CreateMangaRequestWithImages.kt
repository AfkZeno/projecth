package com.backend.presentation.request

import com.backend.domain.enums.MangaStatus
import com.backend.domain.enums.MangaType
import kotlinx.serialization.Serializable

@Serializable
data class CreateMangaRequestWithImages(
    val title: String,
    val alternativeTitles: List<String>? = null,
    val description: String? = null,
    val author: String? = null,
    val artist: String? = null,
    val status: MangaStatus = MangaStatus.ONGOING,
    val type: MangaType = MangaType.MANGA,
    val tags: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val coverKey: String?,
    val bannerKey: String?
)
