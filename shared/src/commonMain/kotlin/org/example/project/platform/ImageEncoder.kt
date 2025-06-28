package org.example.project.platform

expect class ImageEncoder() {
    fun encodeImageToBase64(imagePath: String): String
} 