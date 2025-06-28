package org.example.project.platform

import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions

actual class ImageEncoder {
    actual fun encodeImageToBase64(imagePath: String): String {
        return try {
            println("🔍 iOS ImageEncoder: Starting encoding for path: $imagePath")
            
            val image = UIImage.imageWithContentsOfFile(imagePath)
            if (image == null) {
                println("❌ iOS ImageEncoder: Image not found at path: $imagePath")
                return ""
            }
            
            println("✅ iOS ImageEncoder: Image loaded successfully")
            
            val imageData = UIImagePNGRepresentation(image)
            if (imageData == null) {
                println("❌ iOS ImageEncoder: Failed to create PNG data")
                return ""
            }
            
            println("✅ iOS ImageEncoder: PNG data created, size: ${imageData.length}")
            
            val base64String = imageData.base64EncodedStringWithOptions(0u)
            
            println("✅ iOS ImageEncoder: Base64 encoding completed, length: ${base64String.length}")
            println("🔍 iOS ImageEncoder: Base64 preview: ${base64String.take(50)}...")
            
            base64String
        } catch (e: Exception) {
            println("❌ iOS ImageEncoder: Error encoding image: ${e.message}")
            e.printStackTrace()
            ""
        }
    }
}
