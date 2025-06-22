package org.example.project.platform

import kotlinx.coroutines.flow.Flow
import org.example.project.data.model.ChatMessage
import org.example.project.data.model.ChatSession

class PlatformImpl : Platform {
    override val name: String = "Desktop"
    override val version: String = "1.0.0"
    
    override suspend fun pickImage(): String? {
        // Will be implemented in desktop-specific code
        return null
    }
    
    override suspend fun takePhoto(): String? {
        // Desktop doesn't support camera directly
        return null
    }
    
    override suspend fun shareText(text: String, title: String) {
        // Will be implemented in desktop-specific code
    }
    
    override suspend fun shareImage(imagePath: String, text: String) {
        // Will be implemented in desktop-specific code
    }
    
    override suspend fun saveToGallery(imagePath: String): Boolean {
        // Will be implemented in desktop-specific code
        return false
    }
    
    override suspend fun exportData(sessions: List<ChatSession>, messages: List<ChatMessage>): Boolean {
        // Will be implemented in desktop-specific code
        return false
    }
    
    override fun logEvent(eventName: String, parameters: Map<String, String>) {
        // Desktop analytics - could use local logging or external service
        println("Analytics: $eventName with params: $parameters")
    }
    
    override fun getString(key: String, defaultValue: String): String {
        // Will be implemented with local storage
        return defaultValue
    }
    
    override fun setString(key: String, value: String) {
        // Will be implemented with local storage
    }
    
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        // Will be implemented with local storage
        return defaultValue
    }
    
    override fun setBoolean(key: String, value: Boolean) {
        // Will be implemented with local storage
    }
    
    override fun getThemeMode(): String {
        return getString("theme_mode", "system")
    }
    
    override fun setThemeMode(mode: String) {
        setString("theme_mode", mode)
    }
    
    override fun getLanguage(): String {
        return getString("language", "en")
    }
    
    override fun setLanguage(language: String) {
        setString("language", language)
    }
} 