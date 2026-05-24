package com.backend.presentation.request

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


@Serializable
data class CreateChapterRequest(
    val title: String? = null,
    @Contextual val chapterNumber: Double
)
