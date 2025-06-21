package org.example.project.data.remote

import org.example.project.data.model.GeminiRequest
import org.example.project.data.model.GeminiResponse

interface GeminiApi {
    suspend fun sendMessage(request: GeminiRequest): GeminiResponse
}

class GeminiApiImpl : GeminiApi {
    override suspend fun sendMessage(request: GeminiRequest): GeminiResponse {
        // TODO: Implement actual Gemini API call
        // For now, return mock response
        return GeminiResponse(
            text = "This is a mock response from Gemini API. Your message was: ${request.prompt}",
            usage = null
        )
    }
} 