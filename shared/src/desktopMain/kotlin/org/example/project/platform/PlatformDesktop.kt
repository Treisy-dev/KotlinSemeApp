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
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.example.project.data.model.ChatMessage
import org.example.project.data.model.ChatSession
import javax.swing.SwingUtilities
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.runBlocking

class DesktopPlatform : Platform {
    override val name: String = "Desktop"
    override val version: String = "1.0.0"
    
    private val settingsFile = File(System.getProperty("user.home"), ".semeapp_settings.properties")
    private val properties = Properties()
    
    init {
        println("DesktopPlatform: Initializing DesktopPlatform...")
        loadSettings()
        println("DesktopPlatform: DesktopPlatform initialized successfully")
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
    
    // Альтернативный метод для выбора файла через Compose диалог
    suspend fun pickImageWithComposeDialog(): String? {
        println("DesktopPlatform: pickImageWithComposeDialog called (thread: ${Thread.currentThread().name})")
        
        // Простая реализация - возвращаем тестовый путь для проверки
        // В реальном приложении здесь был бы Compose диалог
        println("DesktopPlatform: Using fallback approach")
        return null
    }
    
    // Простой метод без корутин
    private fun pickImageSimple(): String? {
        println("DesktopPlatform: pickImageSimple called (thread: ${Thread.currentThread().name})")
        try {
            println("DesktopPlatform: Creating JFileChooser...")
            val fileChooser = JFileChooser()
            println("DesktopPlatform: JFileChooser created successfully")
            fileChooser.dialogTitle = "Select Image"
            fileChooser.fileFilter = FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif", "bmp", "webp"
            )
            println("DesktopPlatform: About to call showOpenDialog...")
            val dialogResult = fileChooser.showOpenDialog(null)
            println("DesktopPlatform: JFileChooser result = $dialogResult")
            if (dialogResult == JFileChooser.APPROVE_OPTION) {
                val selectedPath = fileChooser.selectedFile.absolutePath
                println("DesktopPlatform: Selected file = $selectedPath")
                return selectedPath
            } else {
                println("DesktopPlatform: No file selected or dialog cancelled")
                return null
            }
        } catch (e: Exception) {
            println("DesktopPlatform: Exception in pickImageSimple: ${e.message}")
            e.printStackTrace()
            return null
        }
    }
    
    override suspend fun pickImage(): String? {
        println("=== DesktopPlatform: pickImage START ===")
        println("DesktopPlatform: pickImage called (thread: ${Thread.currentThread().name})")
        println("DesktopPlatform: About to call pickImageSimple")
        val result = pickImageSimple()
        println("DesktopPlatform: pickImageSimple returned: $result")
        println("=== DesktopPlatform: pickImage END ===")
        return result
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
    
    // New export functionality
    override suspend fun exportData(sessions: List<ChatSession>, messages: List<ChatMessage>): Boolean = withContext(Dispatchers.IO) {
        try {
            val fileChooser = JFileChooser()
            fileChooser.dialogTitle = "Export Data"
            fileChooser.fileFilter = FileNameExtensionFilter("JSON files", "json")
            fileChooser.selectedFile = File("semeapp_export_${System.currentTimeMillis()}.json")
            
            val result = fileChooser.showSaveDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                val exportData = ExportData(
                    exportDate = kotlinx.datetime.Clock.System.now().toString(),
                    version = version,
                    sessions = sessions,
                    messages = messages
                )
                
                val json = Json { 
                    prettyPrint = true 
                    encodeDefaults = true
                }
                val jsonString = json.encodeToString(exportData)
                
                fileChooser.selectedFile.writeText(jsonString)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            println("Error exporting data: ${e.message}")
            false
        }
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
    
    // Альтернативный метод с runBlocking
    suspend fun pickImageWithRunBlocking(): String? {
        println("DesktopPlatform: pickImageWithRunBlocking called (thread: ${Thread.currentThread().name})")
        
        return runBlocking {
            println("DesktopPlatform: Inside runBlocking (thread: ${Thread.currentThread().name})")
            try {
                val fileChooser = JFileChooser()
                println("DesktopPlatform: JFileChooser created in runBlocking")
                fileChooser.dialogTitle = "Select Image"
                fileChooser.fileFilter = FileNameExtensionFilter(
                    "Image files", "jpg", "jpeg", "png", "gif", "bmp", "webp"
                )
                println("DesktopPlatform: About to call showOpenDialog in runBlocking")
                val dialogResult = fileChooser.showOpenDialog(null)
                println("DesktopPlatform: JFileChooser result in runBlocking = $dialogResult")
                if (dialogResult == JFileChooser.APPROVE_OPTION) {
                    println("DesktopPlatform: Selected file in runBlocking = ${fileChooser.selectedFile.absolutePath}")
                    fileChooser.selectedFile.absolutePath
                } else {
                    println("DesktopPlatform: No file selected in runBlocking")
                    null
                }
            } catch (e: Exception) {
                println("DesktopPlatform: Exception in runBlocking: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }
}

@kotlinx.serialization.Serializable
private data class ExportData(
    val exportDate: String,
    val version: String,
    val sessions: List<ChatSession>,
    val messages: List<ChatMessage>
) 