package org.example.project.platform

import android.content.Context
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class FilePicker(private val context: Context) {
    actual suspend fun pickImage(): String? {
        // Android implementation for picking an image
        return null
    }

    actual suspend fun pickFile(extensions: List<String>): String? {
        // Android implementation for picking a file
        return null
    }
} 