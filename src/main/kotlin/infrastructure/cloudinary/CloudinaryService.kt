package com.backend.infrastructure.cloudinary

import com.backend.presentation.response.UploadResponse
import com.backend.util.allowedTypes
import com.cloudinary.Cloudinary
import io.ktor.http.content.*
import io.ktor.util.logging.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import org.koin.core.component.KoinComponent


class CloudinaryService(private val config: CloudinaryConfig) : KoinComponent {
    private val logger = KtorSimpleLogger("CloudLogger")
    private val maxImageSize = 5 * 1024 * 1024
    private val options = mapOf(
        "folder" to "mangas/covers"
    )

    private val cloudinary: Cloudinary by lazy {
        Cloudinary(
            mapOf(
                "cloud_name" to config.cloudName,
                "api_key" to config.apiKey,
                "api_secret" to config.apiSecret
            )
        )
    }

    suspend fun uploadImage(
        multipart: MultiPartData
    ): UploadResponse {
        var imageUrl: String? = null
        var publicId: String? = null
        var uploaded = false


        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                logger.info("File received: name=${part.originalFileName}, " +
                        "contentType=${part.contentType}")
                validateImage(part)

                val bytes = part
                    .provider()
                    .readRemaining()
                    .readByteArray()
                validateSize(bytes)


                val result = cloudinary
                    .uploader()
                    .upload(
                        bytes,
                        options
                    )
                uploaded = true
                imageUrl = result["secure_url"] as String
                publicId = result["public_id"] as String
            }
            part.release()
        }
        if (!uploaded) {
            throw IllegalArgumentException("No image uploaded")
        }
        return UploadResponse(imageUrl, publicId)
    }

    suspend fun uploadImages(
        multipart: MultiPartData
    ): List<UploadResponse> {
        val uploadedImages = mutableListOf<UploadResponse>()
        multipart.forEachPart { part ->

            if (part is PartData.FileItem) {

                validateImage(part)

                val bytes = part.provider()
                    .readRemaining()
                    .readByteArray()

                validateSize(bytes)

                val result = cloudinary.uploader().upload(
                    bytes,
                    options
                )

                uploadedImages.add(
                    UploadResponse(
                        url = result["secure_url"] as String,
                        publicId = result["public_id"] as String
                    )
                )
            }

            part.release()
        }

        if (uploadedImages.isEmpty()) {
            throw IllegalArgumentException("No images uploaded")
        }

        return uploadedImages
    }

    private fun validateImage(part: PartData): Boolean {
        if (part.contentType !in allowedTypes) {
            throw IllegalArgumentException("Invalid image type")
        }
        return true
    }

    private fun validateSize(bytes: ByteArray): Boolean {
        if (bytes.size > maxImageSize) {
            throw IllegalArgumentException("Image too large")
        }
        logger.info("file size: ${bytes.size}")
        return true
    }

    fun deleteImage(publicId: String): String {
        val deleted = cloudinary.uploader().destroy(publicId, emptyMap<String, String>())
        return deleted["result"] as String
    }
}