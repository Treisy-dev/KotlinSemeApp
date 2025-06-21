package org.example.project.data.local

import kotlinx.coroutines.flow.Flow

interface SettingsStorage {
    fun getThemeMode(): Flow<String>
    suspend fun setThemeMode(mode: String)
    fun getLanguage(): Flow<String>
    suspend fun setLanguage(language: String)
    fun isDarkMode(): Flow<Boolean>
    suspend fun setDarkMode(enabled: Boolean)
    suspend fun clearAllData()
}

class SettingsStorageImpl : SettingsStorage {
    private var themeMode = "system"
    private var language = "en"
    private var darkMode = false
    
    override fun getThemeMode(): Flow<String> {
        return kotlinx.coroutines.flow.flowOf(themeMode)
    }
    
    override suspend fun setThemeMode(mode: String) {
        themeMode = mode
    }
    
    override fun getLanguage(): Flow<String> {
        return kotlinx.coroutines.flow.flowOf(language)
    }
    
    override suspend fun setLanguage(lang: String) {
        language = lang
    }
    
    override fun isDarkMode(): Flow<Boolean> {
        return kotlinx.coroutines.flow.flowOf(darkMode)
    }
    
    override suspend fun setDarkMode(enabled: Boolean) {
        darkMode = enabled
    }
    
    override suspend fun clearAllData() {
        themeMode = "system"
        language = "en"
        darkMode = false
    }
} 