@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
package org.example.project.platform

import kotlinx.cinterop.ExperimentalForeignApi
import org.example.project.data.model.ChatSession
import org.example.project.data.model.ChatMessage
import platform.UIKit.UIDevice
import platform.Foundation.*
import platform.UIKit.UIPasteboard
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIActivityViewController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalForeignApi::class)
actual fun getPlatform(): Platform = IOSPlatform()

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val version: String = UIDevice.currentDevice.systemVersion

    // --- File operations ---
    override suspend fun pickImage(): String? {
        // Для MVP: не реализовано (требует UI взаимодействия)
        return null
    }
    override suspend fun takePhoto(): String? {
        // Для MVP: не реализовано (требует UI взаимодействия)
        return null
    }

    // --- Share functionality ---
    override suspend fun shareText(text: String, title: String) {
        withContext(Dispatchers.Main) {
            val activityItems = listOf(text)
            presentActivityController(activityItems)
        }
    }
    override suspend fun shareImage(imagePath: String, text: String) {
        withContext(Dispatchers.Main) {
            val image = UIImage.imageWithContentsOfFile(imagePath ?: "")
            val items = if (image != null) listOf(image, text) else listOf(text)
            presentActivityController(items)
        }
    }
    private fun presentActivityController(items: List<Any?>) {
        val nonNull = items.filterNotNull()
        val controller = UIActivityViewController(
            activityItems = nonNull,
            applicationActivities = null
        )
        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootVC?.presentViewController(controller, animated = true, completion = null)
    }

    // --- Storage ---
    override suspend fun saveToGallery(imagePath: String): Boolean {
        // Для MVP: не реализовано (требует UI взаимодействия и разрешений)
        return false
    }

    // --- Export functionality ---
    override suspend fun exportData(sessions: List<ChatSession>, messages: List<ChatMessage>): Boolean {
        return withContext(Dispatchers.Main) {
            try {
                val json = Json { prettyPrint = true; encodeDefaults = true }
                val exportData = ExportData(
                    exportDate = NSDate().description.orEmpty(),
                    version = version,
                    sessions = sessions,
                    messages = messages
                )
                val jsonString = json.encodeToString(exportData)
                val ts = NSDate().timeIntervalSince1970.toLong()
                val fileName = "semeapp_export_${ts}.json"
                val docsDir = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true).firstOrNull() as? String ?: NSHomeDirectory()
                val filePath = "$docsDir/$fileName"
                val ns = NSString.create(string = jsonString)
                val ok = ns.writeToFile(
                    filePath,
                    atomically = true,
                    encoding = NSUTF8StringEncoding,
                    error = null
                )
                ok
            } catch (_: Throwable) {
                false
            }
        }
    }

    // --- Analytics ---
    override fun logEvent(eventName: String, parameters: Map<String, String>) {
        println("[iOS Analytics] $eventName: $parameters")
    }

    // --- Settings ---
    private val userDefaults = NSUserDefaults.standardUserDefaults
    override fun getString(key: String, defaultValue: String): String = userDefaults.stringForKey(key) ?: defaultValue
    override fun setString(key: String, value: String) { userDefaults.setObject(value, key) }
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = userDefaults.boolForKey(key)
    override fun setBoolean(key: String, value: Boolean) { userDefaults.setBool(value, key) }
    override fun getThemeMode(): String = getString("theme_mode", "system")
    override fun setThemeMode(mode: String) { setString("theme_mode", mode) }
    override fun getLanguage(): String = getString("language", "en")
    override fun setLanguage(language: String) { setString("language", language) }

    @kotlinx.serialization.Serializable
    private data class ExportData(
        val exportDate: String,
        val version: String,
        val sessions: List<ChatSession>,
        val messages: List<ChatMessage>
    )
}

@OptIn(ExperimentalForeignApi::class)
fun List<Any>.toNSArray(): NSArray {
    val array = NSMutableArray()
    for (item in this) {
        array.addObject(item)
    }
    return array
} 
