package org.example.project.platform

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Clipboard
import java.io.File
import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DesktopPlatform : Platform {
    override val name: String = "Desktop"
    override val version: String = "1.0.0"
    
    private val settingsFile = File(System.getProperty("user.home"), ".semeapp_settings.properties")
    private val properties = Properties()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        if (settingsFile.exists()) {
            try {
                properties.load(FileInputStream(settingsFile))
            } catch (e: Exception) {
                println("Error loading settings: ${e.message}")
            }
        }
    }
    
    private fun saveSettings() {
        try {
            properties.store(FileOutputStream(settingsFile), "SemeApp Settings")
        } catch (e: Exception) {
            println("Error saving settings: ${e.message}")
        }
    }
    
    override suspend fun pickImage(): String? = withContext(Dispatchers.IO) {
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Select Image"
        fileChooser.fileFilter = FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif", "bmp", "webp"
        )
        
        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile.absolutePath
        } else {
            null
        }
    }
    
    override suspend fun takePhoto(): String? {
        // Desktop doesn't support camera directly
        return null
    }
    
    override suspend fun shareText(text: String, title: String) {
        withContext(Dispatchers.IO) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                    // Try to open default email client
                    val mailto = "mailto:?subject=$title&body=${text.replace(" ", "%20")}"
                    Desktop.getDesktop().mail(java.net.URI(mailto))
                } else {
                    // Fallback to clipboard
                    copyToClipboard(text)
                }
            } catch (e: Exception) {
                println("Error sharing text: ${e.message}")
                copyToClipboard(text)
            }
        }
    }
    
    override suspend fun shareImage(imagePath: String, text: String) {
        withContext(Dispatchers.IO) {
            try {
                val file = File(imagePath)
                if (file.exists() && Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file)
                }
            } catch (e: Exception) {
                println("Error sharing image: ${e.message}")
            }
        }
    }
    
    override suspend fun saveToGallery(imagePath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val sourceFile = File(imagePath)
            if (!sourceFile.exists()) return@withContext false
            
            val picturesDir = File(System.getProperty("user.home"), "Pictures")
            if (!picturesDir.exists()) {
                picturesDir.mkdirs()
            }
            
            val destFile = File(picturesDir, "SemeApp_${System.currentTimeMillis()}_${sourceFile.name}")
            sourceFile.copyTo(destFile, overwrite = true)
            true
        } catch (e: Exception) {
            println("Error saving to gallery: ${e.message}")
            false
        }
    }
    
    override fun logEvent(eventName: String, parameters: Map<String, String>) {
        println("Analytics: $eventName with params: $parameters")
    }
    
    override fun getString(key: String, defaultValue: String): String {
        return properties.getProperty(key, defaultValue)
    }
    
    override fun setString(key: String, value: String) {
        properties.setProperty(key, value)
        saveSettings()
    }
    
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return properties.getProperty(key, defaultValue.toString()).toBoolean()
    }
    
    override fun setBoolean(key: String, value: Boolean) {
        properties.setProperty(key, value.toString())
        saveSettings()
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
    
    private suspend fun copyToClipboard(text: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val selection = StringSelection(text)
            clipboard.setContents(selection, selection)
            true
        } catch (e: Exception) {
            println("Error copying to clipboard: ${e.message}")
            false
        }
    }
} 