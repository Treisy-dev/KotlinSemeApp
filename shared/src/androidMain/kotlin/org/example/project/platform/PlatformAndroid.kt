package org.example.project.platform

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.example.project.data.datasource.MessageDao
import org.example.project.data.datasource.SessionDao
import org.example.project.data.mapper.toMessage
import org.example.project.data.mapper.toSession
import org.example.project.data.model.ChatMessage
import org.example.project.data.model.ChatSession
import org.example.project.firebase.FirebaseAnalytics
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AndroidPlatform(
    private val context: Context,
    private val activityProvider: () -> ComponentActivity?,
    private val messageDao: MessageDao,
    private val sessionDao: SessionDao,
    private val firebaseAnalytics: FirebaseAnalytics
) : Platform {

    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val version: String = Build.VERSION.RELEASE

    private val prefs: SharedPreferences
        get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun pickImage(): String? = suspendCoroutine { cont ->
        activityProvider()
            ?.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                cont.resume(uri?.toString())
            }
            ?.launch("image/*")
    }
    
    override suspend fun takePhoto(): String? = suspendCancellableCoroutine { cont ->
        val imageUri = createImageUri()
        val launcher = activityProvider()
            ?.registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    cont.resume(imageUri.toString())
                } else {
                    cont.resume(null)
                }
            }
            ?.launch(imageUri)
    }
    
    override suspend fun shareText(text: String, title: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val chooser = Intent.createChooser(intent, title)
        context.startActivity(chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
    
    override suspend fun shareImage(imagePath: String, text: String) {
        val file = File(imagePath)
        if (!file.exists()) return

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent, "Share Image")
        context.startActivity(chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
    
    override suspend fun saveToGallery(imagePath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val source = File(imagePath)
            if (!source.exists()) return@withContext false

            val filename = source.name
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: return@withContext false

            resolver.openOutputStream(uri)?.use { output ->
                source.inputStream().use { it.copyTo(output) }
            }
            true
        } catch (e: Exception) {
            println("Error saving to gallery: ${e.message}")
            false
        }
    }
    
    override suspend fun exportData(sessions: List<ChatSession>, messages: List<ChatMessage>): Boolean = withContext(Dispatchers.IO) {
        val sessionJob = launch { sessions.forEach { session -> sessionDao.insert(session.toSession()) } }
        val messageJob = launch { messages.forEach { message -> messageDao.insert(message.toMessage()) } }
        sessionJob.join()
        messageJob.join()
        sessionJob.isCompleted && messageJob.isCompleted
    }
    
    override fun logEvent(eventName: String, parameters: Map<String, String>) {
        firebaseAnalytics.logEvent(eventName, parameters)
    }
    
    override fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, null) ?: defaultValue
    }
    
    override fun setString(key: String, value: String) {
        prefs.edit()
            .putString(key, value)
            .apply()
    }
    
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }
    
    override fun setBoolean(key: String, value: Boolean) {
        prefs.edit()
            .putBoolean(key, value)
            .apply()
    }
    
    override fun getThemeMode(): String {
        return getString(KEY_THEME_MODE, THEME_MODE_DEFAULT_VALUE)
    }
    
    override fun setThemeMode(mode: String) {
        setString(KEY_THEME_MODE, mode)
    }
    
    override fun getLanguage(): String {
        return getString(KEY_LANGUAGE, "en")
    }
    
    override fun setLanguage(language: String) {
        setString(KEY_LANGUAGE, language)
    }

    private fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return activityProvider()
            ?.contentResolver
            ?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw IllegalStateException("Failed to create image uri")
    }

    private companion object {

        const val PREFS_NAME = "app_prefs"
        const val KEY_THEME_MODE = "theme_mode"
        const val THEME_MODE_DEFAULT_VALUE = "system"
        const val KEY_LANGUAGE = "language"
    }
}
