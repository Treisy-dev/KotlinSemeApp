package org.example.project.platform

import java.io.File
import java.util.Base64

actual class ImageEncoder {
    actual fun encodeImageToBase64(imagePath: String): String {
        return try {
            val file = File(imagePath)
            if (!file.exists()) {
                println("Image file not found: $imagePath")
                return ""
            }
            
            val bytes = file.readBytes()
            Base64.getEncoder().encodeToString(bytes)
        } catch (e: Exception) {
            println("Error encoding image: ${e.message}")
            ""
        }
    }
} 