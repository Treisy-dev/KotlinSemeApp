package org.example.project.screens

import org.example.project.data.repository.ChatRepository
import org.example.project.data.model.ChatSession
import org.example.project.mvi.BaseViewModel
import org.example.project.mvi.UiState
import org.example.project.mvi.UiEvent
import org.example.project.mvi.UiEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryState(
    val sessions: List<ChatSession> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterType: FilterType = FilterType.ALL
) : UiState

enum class FilterType {
    ALL, WITH_IMAGE, TEXT_ONLY
}

sealed class HistoryEvent : UiEvent {
    object LoadSessions : HistoryEvent()
    data class DeleteSession(val sessionId: String) : HistoryEvent()
    object ClearAllHistory : HistoryEvent()
    data class SetFilter(val filterType: FilterType) : HistoryEvent()
    data class OpenSession(val sessionId: String) : HistoryEvent()
}

sealed class HistoryEffect : UiEffect {
    data class NavigateToChat(val sessionId: String) : HistoryEffect()
    data class ShowError(val message: String) : HistoryEffect()
    data class ShowSuccess(val message: String) : HistoryEffect()
    object ShowClearConfirmation : HistoryEffect()
}

class HistoryViewModel(
    private val chatRepository: ChatRepository
) : BaseViewModel<HistoryState, HistoryEvent, HistoryEffect>(HistoryState()) {

    init {
        handleEvent(HistoryEvent.LoadSessions)
    }

    override fun handleEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.LoadSessions -> {
                viewModelScope.launch {
                    setState { copy(isLoading = true, error = null) }
                    
                    try {
                        chatRepository.getAllSessions().collect { sessions ->
                            val filteredSessions = when (currentState.filterType) {
                                FilterType.ALL -> sessions
                                FilterType.WITH_IMAGE -> sessions.filter { it.hasImage }
                                FilterType.TEXT_ONLY -> sessions.filter { !it.hasImage }
                            }
                            setState { 
                                copy(
                                    sessions = filteredSessions.sortedByDescending { it.timestamp },
                                    isLoading = false
                                ) 
                            }
                        }
                    } catch (e: Exception) {
                        setState { 
                            copy(
                                error = "Failed to load sessions: ${e.message}",
                                isLoading = false
                            ) 
                        }
                    }
                }
            }
            is HistoryEvent.DeleteSession -> {
                viewModelScope.launch {
                    try {
                        chatRepository.deleteSession(event.sessionId)
                        setEffect { HistoryEffect.ShowSuccess("Session deleted") }
                        handleEvent(HistoryEvent.LoadSessions)
                    } catch (e: Exception) {
                        setEffect { HistoryEffect.ShowError("Failed to delete session: ${e.message}") }
                    }
                }
            }
            is HistoryEvent.ClearAllHistory -> {
                viewModelScope.launch {
                    try {
                        chatRepository.clearHistory()
                        setEffect { HistoryEffect.ShowSuccess("All history cleared") }
                        handleEvent(HistoryEvent.LoadSessions)
                    } catch (e: Exception) {
                        setEffect { HistoryEffect.ShowError("Failed to clear history: ${e.message}") }
                    }
                }
            }
            is HistoryEvent.SetFilter -> {
                setState { copy(filterType = event.filterType) }
                handleEvent(HistoryEvent.LoadSessions)
            }
            is HistoryEvent.OpenSession -> {
                setEffect { HistoryEffect.NavigateToChat(event.sessionId) }
            }
        }
    }
} 