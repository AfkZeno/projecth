package com.backend.presentation.response

import kotlinx.serialization.Serializable

@Serializable
data class ChaptersResponse(
    val id: Int? = 0,
    val title: String,
    val chapterNumber: Double,
    val views: Long,
    val createdAt: String
)
