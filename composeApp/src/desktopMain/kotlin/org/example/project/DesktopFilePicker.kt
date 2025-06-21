package org.example.project

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DesktopFilePicker {
    suspend fun pickImage(): String? = suspendCancellableCoroutine { continuation ->
        // This would be implemented with actual file dialog
        // For now, return null
        continuation.resume(null)
    }
    
    suspend fun pickFile(extensions: List<String>): String? = suspendCancellableCoroutine { continuation ->
        // This would be implemented with actual file dialog
        // For now, return null
        continuation.resume(null)
    }
} 