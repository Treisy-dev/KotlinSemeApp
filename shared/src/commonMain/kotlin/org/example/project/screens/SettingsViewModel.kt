package org.example.project.screens

import org.example.project.mvi.BaseViewModel
import org.example.project.mvi.UiEffect
import org.example.project.mvi.UiEvent
import org.example.project.mvi.UiState

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val language: String = "en",
    val isLoading: Boolean = false
) : UiState

sealed class SettingsEvent : UiEvent {
    data object ToggleTheme : SettingsEvent()
    data class ChangeLanguage(val language: String) : SettingsEvent()
    data object ClearHistory : SettingsEvent()
}

sealed class SettingsEffect : UiEffect {
    data object ThemeChanged : SettingsEffect()
    data object LanguageChanged : SettingsEffect()
    data object HistoryCleared : SettingsEffect()
}

class SettingsViewModel : BaseViewModel<SettingsState, SettingsEvent, SettingsEffect>(SettingsState()) {
    
    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleTheme -> {
                setState { copy(isDarkTheme = !isDarkTheme) }
                setEffect { SettingsEffect.ThemeChanged }
            }
            is SettingsEvent.ChangeLanguage -> {
                setState { copy(language = event.language) }
                setEffect { SettingsEffect.LanguageChanged }
            }
            is SettingsEvent.ClearHistory -> {
                setState { copy(isLoading = true) }
                // TODO: Implement history clearing
                setState { copy(isLoading = false) }
                setEffect { SettingsEffect.HistoryCleared }
            }
        }
    }
} 