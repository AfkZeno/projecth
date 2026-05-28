package com.backend.infrastructure.backblaze

data class BackBlazeConfig(
    val bucketName: String,
    val keyId: String,
    val applicationKey: String
)