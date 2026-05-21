package com.backend.domain.model

import kotlin.time.Clock
import kotlin.time.Instant

data class User(
    val id: Int? = null,
    val email: String,
    val name: String,
    val createdAt: Instant = Clock.System.now()
)
