package com.backend.presentation.request

import kotlinx.serialization.Serializable


@Serializable
data class CreateChapterRequest(
    val title: String? = null,
    val chapterNumber: Double
)
