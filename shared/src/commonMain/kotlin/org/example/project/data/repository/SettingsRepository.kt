package org.example.project.data.repository

import org.example.project.data.local.SettingsStorage
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val storage: SettingsStorage
) {
    fun getThemeMode(): Flow<String> {
        return storage.getThemeMode()
    }
    
    suspend fun setThemeMode(mode: String) {
        storage.setThemeMode(mode)
    }
    
    fun getLanguage(): Flow<String> {
        return storage.getLanguage()
    }
    
    suspend fun setLanguage(language: String) {
        storage.setLanguage(language)
    }
    
    fun isDarkMode(): Flow<Boolean> {
        return storage.isDarkMode()
    }
    
    suspend fun setDarkMode(enabled: Boolean) {
        storage.setDarkMode(enabled)
    }
    
    suspend fun clearAllData() {
        storage.clearAllData()
    }
} 