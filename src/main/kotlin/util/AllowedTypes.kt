package com.backend.util

import io.ktor.http.ContentType

val allowedTypes = listOf(
    ContentType.Image.JPEG,
    ContentType.Image.PNG,
    ContentType.Image.WEBP
)
