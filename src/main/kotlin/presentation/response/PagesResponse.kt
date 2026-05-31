package com.backend.presentation.response

import kotlinx.serialization.Serializable

@Serializable
data class PagesResponse(
    val pageNumber: Int,
    val imageKey: String? = null,
    val imageUrl: String? = null,        // URL completa que usará el frontend
    val contentType: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val fileSize: Long? = null,
)
