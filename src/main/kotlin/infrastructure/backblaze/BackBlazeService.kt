package com.backend.infrastructure.backblaze

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.deleteObject
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.presigners.presignGetObject
import aws.sdk.kotlin.services.s3.putObject
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.net.url.Url
import com.backend.domain.model.UploadedPage
import com.backend.infrastructure.webp.ImagePipeline
import com.backend.presentation.response.UploadResponse
import com.backend.util.allowedTypes
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import org.koin.core.component.KoinComponent
import java.util.*
import kotlin.time.Duration.Companion.minutes


class BackBlazeService(private val config: BackBlazeConfig,
    private val imagePipeline: ImagePipeline
) : KoinComponent {
    private val maxImageSize = 4 * 1024 * 1024
    private val s3Client by lazy {
        S3Client {
            region = "us-east-005"
            endpointUrl = Url.parse("https://s3.us-east-005.backblazeb2.com")
            credentialsProvider = StaticCredentialsProvider {
                accessKeyId = config.keyId
                secretAccessKey = config.applicationKey
            }
        }
    }
    suspend fun uploadImage(
        multipart: MultiPartData,
        folder: String = "mangas/covers"
    ): UploadResponse {
        var storageKey: String? = null


        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                println("File received: name=${part.originalFileName}, " +
                        "contentType=${part.contentType}")
                validateImage(part)

                val bytes = part
                    .provider()
                    .readRemaining()
                    .readByteArray()

                validateSize(bytes)


                val fileName = "${UUID.randomUUID()}-${part.originalFileName}"
                val key = "$folder/$fileName"
                s3Client.putObject{
                    this.bucket = config.bucketName
                    this.key = key
                    this.body = ByteStream.fromBytes(bytes)
                    this.contentType = part.contentType?.toString()
                }
                storageKey = key
            }
            part.release()
        }
        if (storageKey == null) {
            throw IllegalArgumentException("No image uploaded")
        }
        val tempUrl = generatePresignedUrl(storageKey, 60)
        return UploadResponse(
            url = tempUrl,
            storageKey = storageKey
        )
    }

    suspend fun generatePresignedUrl(key: String, expiresInMinutes: Long = 60): String {
        val getObjectRequest = GetObjectRequest {
            bucket = config.bucketName
            this.key = key
        }
        val presignedRequest = s3Client.presignGetObject(
            getObjectRequest,
            expiresInMinutes.minutes
        )
        return presignedRequest.url.toString()
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
        println("file size: ${bytes.size}")
        return true
    }

    private fun validateMime(part: PartData.FileItem): Boolean {
        val mime = part.contentType?.toString()
        return mime == "image/webp"
    }

    suspend fun deleteImage(key: String) {
        s3Client.deleteObject {
            bucket = config.bucketName
            this.key = key
        }
    }


    suspend fun uploadChapterPages(
        chapterId: Int,
        multipart: MultiPartData
    ): List<UploadedPage> {
        val uploadedPages = mutableListOf<UploadedPage>()
        var pageNumber = 1
        try {
            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    println("Procesando página $pageNumber: ${part.originalFileName}")
                    validateImage(part)
                    val bytes = part.provider().readRemaining().readByteArray()
                    validateSize(bytes)


                    val extension = part.originalFileName?.substringAfterLast(".") ?: "jpg"
                    val fileName = "${pageNumber.toString().padStart(3, '0')}.$extension"
                    val key = "pages/$chapterId/$fileName"
                    s3Client.putObject {
                        bucket = config.bucketName
                        this.key = key
                        body = ByteStream.fromBytes(bytes)
                        contentType = part.contentType?.toString() ?: "image/jpeg"
                    }
                    uploadedPages.add(
                        UploadedPage(
                            pageNumber = pageNumber,
                            imageKey = key,
                            contentType = part.contentType?.toString(),
                            fileSize = bytes.size.toLong()
                        )
                    )
                    println("Página $pageNumber subida: $key")
                    pageNumber++
                }
                part.release()

            }
            if (uploadedPages.isEmpty()) {
                throw IllegalArgumentException("No se subió ninguna página")
            }
            return uploadedPages
        } catch (e: Exception) {
            println("Error subiendo páginas: ${e.message}")
            throw e
        }

    }

}