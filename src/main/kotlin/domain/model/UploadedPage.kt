package com.backend.domain.model

data class UploadedPage(
    val pageNumber: Int,
    val imageKey: String,
    val contentType: String? = null,
    val fileSize: Long? = null
)
