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
import org.koin.core.component.inject

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
    private val platform: Platform
) : BaseViewModel<PromptState, PromptEvent, PromptEffect>(
    PromptState()
) {
    override fun handleEvent(event: PromptEvent) {
        when (event) {
            is PromptEvent.UpdatePrompt -> updatePrompt(event.text)
            is PromptEvent.SelectImage -> selectImage(event.path)
            is PromptEvent.PickImage -> pickImage()
            is PromptEvent.TakePhoto -> takePhoto()
            is PromptEvent.ClearImage -> clearImage()
            is PromptEvent.SendPrompt -> sendPrompt()
        }
    }

    private fun updatePrompt(text: String) {
        setState { copy(prompt = text) }
    }

    private fun selectImage(path: String) {
        println("PromptViewModel: selectImage called with path: $path")
        setState { copy(imagePath = path) }
    }

    private fun pickImage() {
        println("PromptViewModel: pickImage called")
        println("PromptViewModel: platform = $platform")
        viewModelScope.launch {
            println("PromptViewModel: About to call platform.pickImage()")
            val imagePath = platform.pickImage()
            println("PromptViewModel: platform.pickImage() returned: $imagePath")
            if (imagePath != null) {
                println("PromptViewModel: Setting imagePath to: $imagePath")
                setState { copy(imagePath = imagePath) }
            } else {
                println("PromptViewModel: imagePath is null, not updating state")
            }
        }
    }

    private fun takePhoto() {
        println("PromptViewModel: takePhoto called")
        viewModelScope.launch {
            val imagePath = platform.takePhoto()
            println("PromptViewModel: platform.takePhoto() returned: $imagePath")
            if (imagePath != null) {
                setState { copy(imagePath = imagePath) }
            }
        }
    }

    private fun clearImage() {
        println("PromptViewModel: clearImage called")
        setState { copy(imagePath = null) }
    }

    private fun sendPrompt() {
        if (state.value.prompt.isBlank()) {
            setEffect { PromptEffect.ShowError("Please enter a prompt") }
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            try {
                val sessionId = chatRepository.createSession(state.value.prompt.take(50))
                val result = chatRepository.sendMessage(
                    sessionId = sessionId,
                    content = state.value.prompt,
                    imagePath = state.value.imagePath
                )

                if (result.isSuccess) {
                    setEffect { PromptEffect.NavigateToChat(sessionId) }
                } else {
                    setEffect { PromptEffect.ShowError(result.exceptionOrNull()?.message ?: "Unknown error") }
                }
            } catch (e: Exception) {
                setEffect { PromptEffect.ShowError(e.message ?: "Unknown error") }
            } finally {
                setState { copy(isLoading = false) }
            }
        }
    }
} 