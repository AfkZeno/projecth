package com.backend.infrastructure.webp

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam

class WebpEncoder {
    fun encode(img: BufferedImage, quality: Float): ByteArray {

        val output = ByteArrayOutputStream()

        val writers = ImageIO.getImageWritersByMIMEType("image/webp")
        val writer = writers.next()

        val params = writer.defaultWriteParam
        params.compressionMode = ImageWriteParam.MODE_EXPLICIT
        params.compressionQuality = quality

        ImageIO.createImageOutputStream(output).use { ios ->
            writer.output = ios
            writer.write(null, IIOImage(img, null, null), params)
        }

        writer.dispose()

        return output.toByteArray()
    }
}