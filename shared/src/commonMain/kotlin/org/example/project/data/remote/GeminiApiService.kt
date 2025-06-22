package org.example.project.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.config.GeminiConfig
import org.example.project.data.model.GeminiRequest
import org.example.project.data.model.GeminiResponse
import org.example.project.data.model.ListModelsResponse

class GeminiApiService {
    private val apiKeys = GeminiConfig.API_KEYS
    private var currentKeyIndex = 0
    
    private val baseURL = GeminiConfig.BASE_URL
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }
    
    private val currentApiKey: String
        get() = apiKeys[currentKeyIndex]
    
    private fun nextKey(): Boolean {
        return if (currentKeyIndex + 1 < apiKeys.size) {
            currentKeyIndex++
            println("Switching to next API key: $currentApiKey")
            true
        } else {
            println("All API keys are exhausted.")
            false
        }
    }
    
    suspend fun generateText(prompt: String): Result<String> {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiRequest.Content(
                        parts = listOf(
                            GeminiRequest.Part(text = prompt)
                        )
                    )
                )
            )
            
            val response = performRequest<GeminiResponse>(
                endpoint = "${GeminiConfig.MODEL_NAME}:generateContent",
                request = request
            )
            
            when {
                response.error != null -> {
                    if (response.error.code in listOf(401, 403) && nextKey()) {
                        generateText(prompt)
                    } else {
                        Result.failure(Exception("Gemini API Error: ${response.error.message}"))
                    }
                }
                response.candidates?.isNotEmpty() == true -> {
                    val text = response.candidates.first().content.parts.firstOrNull()?.text
                    if (text != null) {
                        Result.success(text)
                    } else {
                        Result.failure(Exception("No text in response"))
                    }
                }
                else -> Result.failure(Exception("No candidates in response"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun generateTextWithImage(prompt: String, imageBase64: String): Result<String> {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiRequest.Content(
                        parts = listOf(
                            GeminiRequest.Part(text = prompt),
                            GeminiRequest.Part(
                                inlineData = GeminiRequest.Part.InlineData(
                                    mimeType = "image/jpeg",
                                    data = imageBase64
                                )
                            )
                        )
                    )
                )
            )
            
            val response = performRequest<GeminiResponse>(
                endpoint = "${GeminiConfig.MODEL_NAME}:generateContent",
                request = request
            )
            
            when {
                response.error != null -> {
                    if (response.error.code in listOf(401, 403) && nextKey()) {
                        generateTextWithImage(prompt, imageBase64)
                    } else {
                        Result.failure(Exception("Gemini API Error: ${response.error.message}"))
                    }
                }
                response.candidates?.isNotEmpty() == true -> {
                    val text = response.candidates.first().content.parts.firstOrNull()?.text
                    if (text != null) {
                        Result.success(text)
                    } else {
                        Result.failure(Exception("No text in response"))
                    }
                }
                else -> Result.failure(Exception("No candidates in response"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun listModels(): Result<List<String>> {
        return try {
            val response = performRequest<ListModelsResponse>(
                endpoint = "../models",
                request = null
            )
            
            Result.success(response.models.map { it.name })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend inline fun <reified T> performRequest(
        endpoint: String,
        request: Any?
    ): T {
        val url = "$baseURL$endpoint?key=$currentApiKey"
        
        return client.request(url) {
            method = if (request != null) HttpMethod.Post else HttpMethod.Get
            contentType(ContentType.Application.Json)
            
            if (request != null) {
                setBody(request)
            }
        }.body()
    }
    
    fun close() {
        client.close()
    }
} 