package org.example.project.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.platform.getPlatform

class DesktopSettingsStorage : SettingsStorage {
    private val platform = getPlatform()
    
    private val _themeMode = MutableStateFlow(platform.getThemeMode())
    private val _language = MutableStateFlow(platform.getLanguage())
    private val _darkMode = MutableStateFlow(platform.getBoolean("dark_mode", false))
    
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
        platform.setBoolean("dark_mode", enabled)
        _darkMode.value = enabled
    }
    
    override suspend fun clearAllData() {
        platform.setThemeMode("system")
        platform.setLanguage("en")
        platform.setBoolean("dark_mode", false)
        
        _themeMode.value = "system"
        _language.value = "en"
        _darkMode.value = false
    }
} 