package org.example.project.screens

import org.example.project.data.model.ChatMessage
import org.example.project.mvi.UiEvent
import org.example.project.mvi.UiEffect

sealed class ChatEvent : UiEvent {
    data class UpdateMessage(val text: String) : ChatEvent()
    data class SendMessage(val content: String) : ChatEvent()
    data class LoadSession(val sessionId: String) : ChatEvent()
    data class ShareMessage(val message: ChatMessage) : ChatEvent()
    object ClearError : ChatEvent()
}

sealed class ChatEffect : UiEffect {
    data class ShowError(val message: String) : ChatEffect()
    data class ShowSuccess(val message: String) : ChatEffect()
    object NavigateToSettings : ChatEffect()
    object NavigateToHistory : ChatEffect()
} 