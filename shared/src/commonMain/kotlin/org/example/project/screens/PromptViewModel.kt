package org.example.project.screens

import org.example.project.data.repository.ChatRepository
import org.example.project.data.repository.SettingsRepository
import org.example.project.platform.Platform
import org.example.project.mvi.BaseViewModel
import org.example.project.mvi.UiState
import org.example.project.mvi.UiEvent
import org.example.project.mvi.UiEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PromptState(
    val prompt: String = "",
    val imagePath: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

sealed class PromptEvent : UiEvent {
    data class UpdatePrompt(val text: String) : PromptEvent()
    data class SelectImage(val path: String) : PromptEvent()
    object PickImage : PromptEvent()
    object TakePhoto : PromptEvent()
    object SendPrompt : PromptEvent()
    object ClearImage : PromptEvent()
}

sealed class PromptEffect : UiEffect {
    data class NavigateToChat(val sessionId: String) : PromptEffect()
    data class ShowError(val message: String) : PromptEffect()
    data class ShowSuccess(val message: String) : PromptEffect()
}

class PromptViewModel(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository,
    private val platform: Platform
) : BaseViewModel<PromptState, PromptEvent, PromptEffect>(PromptState()) {

    override fun handleEvent(event: PromptEvent) {
        when (event) {
            is PromptEvent.UpdatePrompt -> {
                setState { copy(prompt = event.text) }
            }
            is PromptEvent.SelectImage -> {
                setState { copy(imagePath = event.path) }
            }
            is PromptEvent.PickImage -> {
                viewModelScope.launch {
                    try {
                        val imagePath = platform.pickImage()
                        if (imagePath != null) {
                            setState { copy(imagePath = imagePath) }
                        }
                    } catch (e: Exception) {
                        setEffect { PromptEffect.ShowError("Failed to pick image: ${e.message}") }
                    }
                }
            }
            is PromptEvent.TakePhoto -> {
                viewModelScope.launch {
                    try {
                        val imagePath = platform.takePhoto()
                        if (imagePath != null) {
                            setState { copy(imagePath = imagePath) }
                        }
                    } catch (e: Exception) {
                        setEffect { PromptEffect.ShowError("Failed to take photo: ${e.message}") }
                    }
                }
            }
            is PromptEvent.SendPrompt -> {
                if (currentState.prompt.isBlank()) {
                    setEffect { PromptEffect.ShowError("Please enter a prompt") }
                    return
                }
                
                viewModelScope.launch {
                    setState { copy(isLoading = true, error = null) }
                    
                    try {
                        val sessionId = chatRepository.createSession(currentState.prompt.take(50))
                        val result = chatRepository.sendMessage(
                            sessionId = sessionId,
                            content = currentState.prompt,
                            imagePath = currentState.imagePath
                        )
                        
                        if (result.isSuccess) {
                            setEffect { PromptEffect.NavigateToChat(sessionId) }
                            setEffect { PromptEffect.ShowSuccess("Message sent successfully") }
                        } else {
                            setEffect { PromptEffect.ShowError("Failed to send message: ${result.exceptionOrNull()?.message}") }
                        }
                    } catch (e: Exception) {
                        setEffect { PromptEffect.ShowError("Error: ${e.message}") }
                    } finally {
                        setState { copy(isLoading = false) }
                    }
                }
            }
            is PromptEvent.ClearImage -> {
                setState { copy(imagePath = null) }
            }
        }
    }
} 