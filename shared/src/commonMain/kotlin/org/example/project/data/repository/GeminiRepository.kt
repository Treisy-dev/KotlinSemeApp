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
                println("🔍 GeminiRepository: Starting image encoding for path: $imagePath")
                val imageBase64 = imageEncoder.encodeImageToBase64(imagePath)
                println("🔍 GeminiRepository: Image encoding result length: ${imageBase64.length}")
                println("🔍 GeminiRepository: Image encoding result preview: ${imageBase64.take(100)}...")
                
                if (imageBase64.isNotEmpty()) {
                    // Проверяем, что base64 строка не содержит переносы строк
                    val cleanBase64 = imageBase64.replace("\n", "").replace("\r", "")
                    println("🔍 GeminiRepository: Clean base64 length: ${cleanBase64.length}")
                    println("🔍 GeminiRepository: Clean base64 preview: ${cleanBase64.take(100)}...")
                    
                    apiService.generateTextWithImage(prompt, cleanBase64)
                } else {
                    println("❌ GeminiRepository: Failed to encode image - empty result")
                    Result.failure(Exception("Failed to encode image"))
                }
            } else {
                apiService.generateText(prompt)
            }
            
            result.fold(
                onSuccess = { text ->
                    println("✅ GeminiRepository: Successfully received response")
                    LegacyGeminiResponse(
                        text = text,
                        usage = null // Gemini API v1beta не возвращает usage
                    )
                },
                onFailure = { error ->
                    println("❌ GeminiRepository: Error occurred: ${error.message}")
                    LegacyGeminiResponse(
                        text = "Error: ${error.message}",
                        usage = null
                    )
                }
            )
        } catch (e: Exception) {
            println("❌ GeminiRepository: Exception occurred: ${e.message}")
            e.printStackTrace()
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