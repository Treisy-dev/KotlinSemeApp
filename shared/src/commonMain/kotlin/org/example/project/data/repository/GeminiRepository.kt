package org.example.project.data.repository

import org.example.project.data.remote.GeminiApi
import org.example.project.data.model.GeminiRequest
import org.example.project.data.model.GeminiResponse

class GeminiRepository(
    private val api: GeminiApi
) {
    suspend fun sendMessage(
        prompt: String,
        imagePath: String? = null
    ): GeminiResponse {
        val request = GeminiRequest(
            prompt = prompt,
            imageData = imagePath?.let { encodeImageToBase64(it) }
        )
        
        return api.sendMessage(request)
    }
    
    private fun encodeImageToBase64(imagePath: String): String {
        // Will be implemented to encode image to base64
        return ""
    }
} 