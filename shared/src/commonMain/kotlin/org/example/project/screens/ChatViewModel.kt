package org.example.project.screens

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.mvi.BaseViewModel
import org.example.project.mvi.UiEffect
import org.example.project.mvi.UiEvent
import org.example.project.mvi.UiState

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val selectedImageUrl: String? = null,
    val isLoading: Boolean = false
) : UiState

sealed class ChatEvent : UiEvent {
    data class UpdateInputText(val text: String) : ChatEvent()
    data object SendMessage : ChatEvent()
    data class SelectImage(val imageUrl: String) : ChatEvent()
    data object ClearSelectedImage : ChatEvent()
    data object OpenCamera : ChatEvent()
    data object OpenGallery : ChatEvent()
}

sealed class ChatEffect : UiEffect {
    data object MessageSent : ChatEffect()
    data object ErrorOccurred : ChatEffect()
    data object OpenCameraRequested : ChatEffect()
    data object OpenGalleryRequested : ChatEffect()
}

class ChatViewModel : BaseViewModel<ChatState, ChatEvent, ChatEffect>(ChatState()) {
    
    override fun handleEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.UpdateInputText -> {
                setState { copy(inputText = event.text) }
            }
            is ChatEvent.SendMessage -> {
                val text = currentState.inputText.trim()
                if (text.isNotEmpty() || currentState.selectedImageUrl != null) {
                    sendMessage(text)
                }
            }
            is ChatEvent.SelectImage -> {
                setState { copy(selectedImageUrl = event.imageUrl) }
            }
            is ChatEvent.ClearSelectedImage -> {
                setState { copy(selectedImageUrl = null) }
            }
            is ChatEvent.OpenCamera -> {
                setEffect { ChatEffect.OpenCameraRequested }
            }
            is ChatEvent.OpenGallery -> {
                setEffect { ChatEffect.OpenGalleryRequested }
            }
        }
    }
    
    private fun sendMessage(text: String) {
        setState { 
            copy(
                messages = messages + ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    text = text,
                    isUser = true,
                    imageUrl = selectedImageUrl
                ),
                inputText = "",
                selectedImageUrl = null,
                isLoading = true
            )
        }
        
        // Simulate AI response
        // TODO: Replace with actual Gemini API call
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // Simulate network delay
            
            val responseText = if (currentState.selectedImageUrl != null) {
                "I can see the image you sent! This is a simulated response from Gemini AI for image analysis. Your message was: $text"
            } else {
                "This is a simulated response from Gemini AI. Your message was: $text"
            }
            
            setState { 
                copy(
                    messages = messages + ChatMessage(
                        id = System.currentTimeMillis().toString(),
                        text = responseText,
                        isUser = false
                    ),
                    isLoading = false
                )
            }
            setEffect { ChatEffect.MessageSent }
        }
    }
} 