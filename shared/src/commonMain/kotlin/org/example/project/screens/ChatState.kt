package org.example.project.screens

import org.example.project.data.model.ChatMessage
import org.example.project.mvi.UiState

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val currentMessage: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessionId: String? = null
) : UiState 