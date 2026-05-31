package com.backend.infrastructure.webp

import com.backend.domain.model.Size

object ImagePolicy {
    val COVER_SIZE = Size(400, 600)   // ratio 2:3
    val BANNER_SIZE = Size(1600, 900)  // ratio 16:9

    const val PAGE_MAX_WIDTH = 1440
    const val PAGE_MAX_HEIGHT = 4096
}