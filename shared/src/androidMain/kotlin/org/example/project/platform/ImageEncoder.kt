package org.example.project.platform

import android.util.Base64
import java.io.File

actual class ImageEncoder {
    actual fun encodeImageToBase64(imagePath: String): String {
        return try {
            val file = File(imagePath)
            if (!file.exists()) {
                println("Image file not found: $imagePath")
                return ""
            }
            val bytes = file.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            println("Error encoding image: ${e.message}")
            ""
        }
    }
} 
