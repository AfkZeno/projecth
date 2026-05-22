package com.backend.presentation.response

import kotlinx.serialization.Serializable


@Serializable
data class HealthResponse(
    val message: String = "Ping"
)