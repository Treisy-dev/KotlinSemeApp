package org.example.project.platform

import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.NSDataBase64Encoding64CharacterLineLength

actual class ImageEncoder {
    actual fun encodeImageToBase64(imagePath: String): String {
        return try {
            val image = UIImage.imageWithContentsOfFile(imagePath)
            if (image == null) {
                println("Image not found at path: $imagePath")
                return ""
            }
            val imageData = UIImageJPEGRepresentation(image, 0.8)
            imageData?.base64EncodedStringWithOptions(NSDataBase64Encoding64CharacterLineLength) ?: ""
        } catch (e: Exception) {
            println("Error encoding image: ${e.message}")
            ""
        }
    }
}
