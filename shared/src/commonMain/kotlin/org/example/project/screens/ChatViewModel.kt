package org.example.project.screens

import org.example.project.data.repository.ChatRepository
import org.example.project.data.repository.SettingsRepository
import org.example.project.share.ShareSheet
import org.example.project.mvi.BaseViewModel
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository,
    private val shareSheet: ShareSheet
) : BaseViewModel<ChatState, ChatEvent, ChatEffect>(ChatState()) {

    override fun handleEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.UpdateMessage -> {
                println("ChatViewModel: UpdateMessage event received with text: '${event.text}'")
                setState { copy(currentMessage = event.text) }
                println("ChatViewModel: State updated, currentMessage is now: '${currentState.currentMessage}'")
            }
            is ChatEvent.SendMessage -> {
                if (event.content.isBlank()) return
                
                viewModelScope.launch {
                    setState { copy(isLoading = true, error = null) }
                    
                    try {
                        val sessionId = currentState.sessionId ?: 
                            chatRepository.createSession(event.content.take(50))
                        
                        if (currentState.sessionId == null) {
                            setState { copy(sessionId = sessionId) }
                        }
                        
                        val result = chatRepository.sendMessage(
                            sessionId = sessionId,
                            content = event.content
                        )
                        
                        if (result.isSuccess) {
                            setState { copy(currentMessage = "") }
                            loadMessages(sessionId)
                        } else {
                            setEffect { ChatEffect.ShowError("Failed to send message: ${result.exceptionOrNull()?.message}") }
                        }
                    } catch (e: Exception) {
                        setEffect { ChatEffect.ShowError("Error: ${e.message}") }
                    } finally {
                        setState { copy(isLoading = false) }
                    }
                }
            }
            is ChatEvent.LoadSession -> {
                loadMessages(event.sessionId)
                setState { copy(sessionId = event.sessionId) }
            }
            is ChatEvent.ShareMessage -> {
                viewModelScope.launch {
                    try {
                        val success = shareSheet.copyToClipboard(event.message.content)
                        if (success) {
                            setEffect { ChatEffect.ShowSuccess("Message copied to clipboard") }
                        } else {
                            setEffect { ChatEffect.ShowError("Failed to copy message to clipboard") }
                        }
                    } catch (e: Exception) {
                        setEffect { ChatEffect.ShowError("Failed to copy message: ${e.message}") }
                    }
                }
            }
            is ChatEvent.ClearError -> {
                setState { copy(error = null) }
            }
        }
    }
    
    private fun loadMessages(sessionId: String) {
        viewModelScope.launch {
            try {
                chatRepository.getMessages(sessionId).collect { messages ->
                    setState { copy(messages = messages) }
                }
            } catch (e: Exception) {
                setEffect { ChatEffect.ShowError("Failed to load messages: ${e.message}") }
            }
        }
    }
} 