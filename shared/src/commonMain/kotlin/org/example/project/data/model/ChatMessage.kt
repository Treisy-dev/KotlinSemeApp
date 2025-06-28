package org.example.project.data.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class ChatMessage(
    val id: String,
    val content: String,
    val imagePath: String? = null,
    val isUser: Boolean,
    val timestamp: Instant,
    val sessionId: String
)

@Serializable
data class ChatSession(
    val id: String,
    val title: String,
    val lastMessage: String,
    val timestamp: Instant,
    val hasImage: Boolean = false
) 