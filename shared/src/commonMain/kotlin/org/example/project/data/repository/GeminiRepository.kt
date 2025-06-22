package org.example.project.data.repository

import org.example.project.data.remote.GeminiApiService
import org.example.project.data.model.LegacyGeminiResponse
import org.example.project.platform.ImageEncoder

class GeminiRepository(
    private val apiService: GeminiApiService,
    private val imageEncoder: ImageEncoder
) {
    suspend fun sendMessage(
        prompt: String,
        imagePath: String? = null
    ): LegacyGeminiResponse {
        return try {
            val result = if (imagePath != null) {
                val imageBase64 = imageEncoder.encodeImageToBase64(imagePath)
                if (imageBase64.isNotEmpty()) {
                    apiService.generateTextWithImage(prompt, imageBase64)
                } else {
                    Result.failure(Exception("Failed to encode image"))
                }
            } else {
                apiService.generateText(prompt)
            }
            
            result.fold(
                onSuccess = { text ->
                    LegacyGeminiResponse(
                        text = text,
                        usage = null // Gemini API v1beta не возвращает usage
                    )
                },
                onFailure = { error ->
                    LegacyGeminiResponse(
                        text = "Error: ${error.message}",
                        usage = null
                    )
                }
            )
        } catch (e: Exception) {
            LegacyGeminiResponse(
                text = "Error: ${e.message}",
                usage = null
            )
        }
    }
    
    suspend fun listModels(): List<String> {
        return try {
            apiService.listModels().getOrElse { emptyList() }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun close() {
        apiService.close()
    }
} 