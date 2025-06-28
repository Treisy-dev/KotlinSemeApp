package org.example.project.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<Content>
) {
    @Serializable
    data class Content(
        val parts: List<Part>
    )
    
    @Serializable
    data class Part(
        val text: String? = null,
        val inlineData: InlineData? = null
    ) {
        @Serializable
        data class InlineData(
            val mimeType: String,
            val data: String
        )
    }
}

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null,
    val error: GeminiError? = null
) {
    @Serializable
    data class Candidate(
        val content: Content
    ) {
        @Serializable
        data class Content(
            val parts: List<Part>
        ) {
            @Serializable
            data class Part(
                val text: String? = null
            )
        }
    }
    
    @Serializable
    data class GeminiError(
        val code: Int,
        val message: String,
        val status: String
    )
}

@Serializable
data class ListModelsResponse(
    val models: List<Model>
) {
    @Serializable
    data class Model(
        val name: String,
        val supportedMethods: List<String>? = null
    )
}

// Legacy models for backward compatibility
@Serializable
data class LegacyGeminiRequest(
    val prompt: String,
    val imageData: String? = null,
    val maxTokens: Int = 1000
)

@Serializable
data class LegacyGeminiResponse(
    val text: String,
    val usage: Usage? = null
) {
    @Serializable
    data class Usage(
        val promptTokens: Int,
        val completionTokens: Int,
        val totalTokens: Int
    )
} 