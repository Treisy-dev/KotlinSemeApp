package org.example.project.screens

import org.example.project.data.repository.SettingsRepository
import org.example.project.localization.LocalizationManagerProvider
import org.example.project.platform.getPlatform
import org.example.project.ui.design.ThemeManagerProvider
import org.example.project.mvi.BaseViewModel
import org.example.project.mvi.UiState
import org.example.project.mvi.UiEvent
import org.example.project.mvi.UiEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

data class SettingsState(
    val isDarkMode: Boolean = false,
    val themeMode: String = "system",
    val language: String = "en",
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

sealed class SettingsEvent : UiEvent {
    data class SetDarkMode(val enabled: Boolean) : SettingsEvent()
    data class SetThemeMode(val mode: String) : SettingsEvent()
    data class SetLanguage(val language: String) : SettingsEvent()
    object LoadSettings : SettingsEvent()
    object ClearAllData : SettingsEvent()
    object ClearError : SettingsEvent()
}

sealed class SettingsEffect : UiEffect {
    data class ShowError(val message: String) : SettingsEffect()
    data class ShowSuccess(val message: String) : SettingsEffect()
    object ShowClearConfirmation : SettingsEffect()
}

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<SettingsState, SettingsEvent, SettingsEffect>(SettingsState()) {

    private val localizationManager = LocalizationManagerProvider.getInstance()
    private val themeManager = ThemeManagerProvider.getInstance()
    private val platform = getPlatform()

    init {
        handleEvent(SettingsEvent.LoadSettings)
    }

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetDarkMode -> {
                viewModelScope.launch {
                    try {
                        settingsRepository.setDarkMode(event.enabled)
                        themeManager.setDarkMode(event.enabled)
                        setState { copy(isDarkMode = event.enabled) }
                        setEffect { SettingsEffect.ShowSuccess("Theme updated") }
                    } catch (e: Exception) {
                        setEffect { SettingsEffect.ShowError("Failed to update theme: ${e.message}") }
                    }
                }
            }
            is SettingsEvent.SetThemeMode -> {
                viewModelScope.launch {
                    try {
                        settingsRepository.setThemeMode(event.mode)
                        themeManager.setThemeMode(event.mode)
                        setState { copy(themeMode = event.mode) }
                        setEffect { SettingsEffect.ShowSuccess("Theme mode updated") }
                    } catch (e: Exception) {
                        setEffect { SettingsEffect.ShowError("Failed to update theme mode: ${e.message}") }
                    }
                }
            }
            is SettingsEvent.SetLanguage -> {
                viewModelScope.launch {
                    try {
                        settingsRepository.setLanguage(event.language)
                        localizationManager.setLanguage(event.language)
                        setState { copy(language = event.language) }
                        setEffect { SettingsEffect.ShowSuccess("Language updated") }
                    } catch (e: Exception) {
                        setEffect { SettingsEffect.ShowError("Failed to update language: ${e.message}") }
                    }
                }
            }
            is SettingsEvent.LoadSettings -> {
                viewModelScope.launch {
                    setState { copy(isLoading = true, error = null) }
                    
                    try {
                        // Загружаем настройки из хранилища
                        val storedLanguage = settingsRepository.getLanguage().first()
                        val storedThemeMode = settingsRepository.getThemeMode().first()
                        val storedDarkMode = settingsRepository.isDarkMode().first()
                        
                        // Обновляем состояние
                        setState { 
                            copy(
                                language = storedLanguage,
                                themeMode = storedThemeMode,
                                isDarkMode = storedDarkMode
                            ) 
                        }
                        
                        // Синхронизируем с платформой, LocalizationManager и ThemeManager
                        platform.setLanguage(storedLanguage)
                        localizationManager.syncLanguage(storedLanguage)
                        
                    } catch (e: Exception) {
                        setState { copy(error = "Failed to load settings: ${e.message}") }
                    } finally {
                        setState { copy(isLoading = false) }
                    }
                }
            }
            is SettingsEvent.ClearAllData -> {
                viewModelScope.launch {
                    try {
                        settingsRepository.clearAllData()
                        setEffect { SettingsEffect.ShowSuccess("All data cleared") }
                        handleEvent(SettingsEvent.LoadSettings)
                    } catch (e: Exception) {
                        setEffect { SettingsEffect.ShowError("Failed to clear data: ${e.message}") }
                    }
                }
            }
            is SettingsEvent.ClearError -> {
                setState { copy(error = null) }
            }
        }
    }
} 