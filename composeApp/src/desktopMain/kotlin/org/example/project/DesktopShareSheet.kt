package org.example.project

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI

class DesktopShareSheet {
    suspend fun shareText(text: String, title: String) {
        try {
            // Try to copy to clipboard
            val selection = StringSelection(text)
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(selection, selection)
            
            // Try to open default email client
            if (Desktop.isDesktopSupported()) {
                val desktop = Desktop.getDesktop()
                if (desktop.isSupported(Desktop.Action.MAIL)) {
                    val mailto = "mailto:?subject=$title&body=${text.replace(" ", "%20")}"
                    desktop.mail(URI(mailto))
                }
            }
        } catch (e: Exception) {
            // Fallback: just copy to clipboard
            val selection = StringSelection(text)
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(selection, selection)
        }
    }
    
    suspend fun shareImage(imagePath: String, text: String) {
        // For desktop, we can't easily share images
        // Just share the text with image path
        shareText("$text\nImage: $imagePath", "Shared Image")
    }
    
    suspend fun copyToClipboard(text: String): Boolean {
        return try {
            val selection = StringSelection(text)
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(selection, selection)
            true
        } catch (e: Exception) {
            false
        }
    }
} 