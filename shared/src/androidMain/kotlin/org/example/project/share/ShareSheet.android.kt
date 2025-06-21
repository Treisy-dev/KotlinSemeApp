package org.example.project.share

import android.content.Context
import android.content.Intent

actual fun createShareSheet(): ShareSheet {
    return object : ShareSheet {
        override fun shareText(text: String, title: String) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                putExtra(Intent.EXTRA_SUBJECT, title)
            }
            
            // Note: This would need to be called from an Activity context
            // For now, we'll just log the intent
            println("Share Intent: $intent")
        }
    }
} 