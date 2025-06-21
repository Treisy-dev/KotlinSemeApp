package org.example.project.platform

import kotlinx.coroutines.flow.Flow

interface Platform {
    val name: String
    val version: String
    
    // File operations
    suspend fun pickImage(): String?
    suspend fun takePhoto(): String?
    
    // Share functionality
    suspend fun shareText(text: String, title: String = "Share")
    suspend fun shareImage(imagePath: String, text: String = "")
    
    // Storage
    suspend fun saveToGallery(imagePath: String): Boolean
    
    // Analytics
    fun logEvent(eventName: String, parameters: Map<String, String> = emptyMap())
    
    // Settings
    fun getString(key: String, defaultValue: String = ""): String
    fun setString(key: String, value: String)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun setBoolean(key: String, value: Boolean)
    
    // Theme
    fun getThemeMode(): String
    fun setThemeMode(mode: String)
    
    // Language
    fun getLanguage(): String
    fun setLanguage(language: String)
} 