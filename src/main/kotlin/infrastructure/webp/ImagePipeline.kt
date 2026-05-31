package com.backend.infrastructure.webp

import com.backend.domain.enums.ImageType
import org.imgscalr.Scalr
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

class ImagePipeline(
    private val encoder: WebpEncoder
) {
    fun process(
        bytes: ByteArray,
        type: ImageType
    ): ByteArray {
        val image = decode(bytes)

        val processed = when (type) {
            ImageType.COVER -> resizeCover(image)
            ImageType.BANNER -> resizeBanner(image)
            ImageType.PAGE -> resizePage(image)
        }
        return encoder.encode(processed, quality = 0.80f)

    }


    private fun decode(bytes: ByteArray): BufferedImage {
        val img = ImageIO.read(ByteArrayInputStream(bytes))
            ?: throw IllegalArgumentException("Invalid image")

        return img
    }

    private fun resizeCover(img: BufferedImage): BufferedImage {
        val target = ImagePolicy.COVER_SIZE

        return Scalr.resize(
            img,
            Scalr.Method.QUALITY,
            Scalr.Mode.FIT_EXACT,
            target.width,
            target.height
        )
    }
    private fun resizeBanner(img: BufferedImage): BufferedImage {
        val target = ImagePolicy.BANNER_SIZE

        return Scalr.resize(
            img,
            Scalr.Method.QUALITY,
            Scalr.Mode.FIT_EXACT,
            target.width,
            target.height
        )
    }
    private fun resizePage(img: BufferedImage): BufferedImage {

        val maxW = ImagePolicy.PAGE_MAX_WIDTH
        val maxH = ImagePolicy.PAGE_MAX_HEIGHT

        val widthRatio = maxW.toDouble() / img.width.toDouble()
        val heightRatio = maxH.toDouble() / img.height.toDouble()

        val ratio = minOf(widthRatio, heightRatio, 1.0)

        if (ratio == 1.0) return img

        val newW = (img.width * ratio).toInt()
        val newH = (img.height * ratio).toInt()

        return Scalr.resize(
            img,
            Scalr.Method.QUALITY,
            Scalr.Mode.FIT_TO_WIDTH,
            newW,
            newH
        )
    }
}