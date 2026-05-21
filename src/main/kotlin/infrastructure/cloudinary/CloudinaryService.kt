package com.backend.infrastructure.cloudinary

import com.cloudinary.Cloudinary
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import org.koin.core.component.KoinComponent


class CloudinaryService(private val config: CloudinaryConfig) : KoinComponent {
    private val cloudinary: Cloudinary by lazy {
        Cloudinary(mapOf(
            "cloud_name" to config.cloudName,
            "api_key" to config.apiKey,
            "api_secret" to config.apiSecret
        ))
    }

    suspend fun uploadImage(
        file: PartData.FileItem,
        folder: String = "mangas/covers",
        publicId: String? = null
    ): String {
        val bytes = file.provider().readRemaining().readByteArray()

        val uploadParams = mutableMapOf<String, Any>(
            "folder" to folder,
            "resource_type" to "image",
            "quality" to "auto",
            "fetch_format" to "auto"
        )
        publicId?.let { uploadParams["public_id"] = it }
        val result = cloudinary.uploader().upload(bytes, uploadParams)
        return result["secure_url"] as String
    }
    suspend fun uploadImages(
        fileItems: List<PartData.FileItem>,
        folder: String = "mangas/chapters"
    ): List<String> {
        return fileItems.map { fileItem ->
            uploadImage(fileItem, folder)
        }
    }
}