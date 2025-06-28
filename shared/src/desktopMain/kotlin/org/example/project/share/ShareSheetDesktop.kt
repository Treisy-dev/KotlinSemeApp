package org.example.project.share

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DesktopShareSheet : ShareSheet {
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
    
    override suspend fun copyToClipboard(text: String): Boolean = withContext(Dispatchers.IO) {
        try {
            println("DesktopShareSheet: Attempting to copy text to clipboard: '${text.take(50)}...'")
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val selection = StringSelection(text)
            clipboard.setContents(selection, selection)
            println("DesktopShareSheet: Successfully copied text to clipboard")
            true
        } catch (e: Exception) {
            println("DesktopShareSheet: Error copying to clipboard: ${e.message}")
            e.printStackTrace()
            false
        }
    }
} 