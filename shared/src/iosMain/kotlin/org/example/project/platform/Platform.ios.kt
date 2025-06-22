package org.example.project.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIDevice
import org.example.project.data.model.ChatSession
import org.example.project.data.model.ChatMessage

@OptIn(ExperimentalForeignApi::class)
actual fun getPlatform(): Platform = IOSPlatform()

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val version: String = UIDevice.currentDevice.systemVersion
    override suspend fun pickImage(): String? = TODO("Not yet implemented")
    override suspend fun takePhoto(): String? = TODO("Not yet implemented")
    override suspend fun shareText(text: String, title: String) { TODO("Not yet implemented") }
    override suspend fun shareImage(imagePath: String, text: String) { TODO("Not yet implemented") }
    override suspend fun saveToGallery(imagePath: String): Boolean = TODO("Not yet implemented")
    override suspend fun exportData(sessions: List<ChatSession>, messages: List<ChatMessage>): Boolean = TODO("Not yet implemented")
    override fun logEvent(eventName: String, parameters: Map<String, String>) { TODO("Not yet implemented") }
    override fun getString(key: String, defaultValue: String): String = defaultValue
    override fun setString(key: String, value: String) { }
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = defaultValue
    override fun setBoolean(key: String, value: Boolean) { }
    override fun getThemeMode(): String = "system"
    override fun setThemeMode(mode: String) { }
    override fun getLanguage(): String = "en"
    override fun setLanguage(language: String) { }
} 