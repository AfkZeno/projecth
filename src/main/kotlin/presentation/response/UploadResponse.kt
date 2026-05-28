package com.backend.presentation.response

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse (
    val url: String?,
    val storageKey: String?,
)