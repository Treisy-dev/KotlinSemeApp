package org.example.project.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.platform.getPlatform

internal class AndroidSettingsStorage : SettingsStorage {

    private val platform = getPlatform()

    private val _themeMode = MutableStateFlow(platform.getThemeMode())
    private val _language = MutableStateFlow(platform.getLanguage())
    private val _darkMode = MutableStateFlow(platform.getBoolean(KEY_DARK_MODE, DARK_MODE_DEFAULT_VALUE))

    override fun getThemeMode(): Flow<String> = _themeMode.asStateFlow()

    override suspend fun setThemeMode(mode: String) {
        platform.setThemeMode(mode)
        _themeMode.value = mode
    }

    override fun getLanguage(): Flow<String> = _language.asStateFlow()

    override suspend fun setLanguage(language: String) {
        platform.setLanguage(language)
        _language.value = language
    }

    override fun isDarkMode(): Flow<Boolean> = _darkMode.asStateFlow()

    override suspend fun setDarkMode(enabled: Boolean) {
        platform.setBoolean(KEY_DARK_MODE, enabled)
        _darkMode.value = enabled
    }

    override suspend fun clearAllData() {
        platform.setThemeMode("system")
        platform.setLanguage("en")
        platform.setBoolean("dark_mode", false)

        _themeMode.value = THEME_MODE_DEFAULT_VALUE
        _language.value = LANGUAGE_DEFAULT_VALUE
        _darkMode.value = DARK_MODE_DEFAULT_VALUE
    }

    private companion object {

        const val KEY_DARK_MODE = "dark_mode"
        const val DARK_MODE_DEFAULT_VALUE = false
        const val THEME_MODE_DEFAULT_VALUE = "system"
        const val LANGUAGE_DEFAULT_VALUE = "en"
    }
}
