package org.example.project.share

class ShareSheetImpl : ShareSheet {
    override suspend fun shareText(text: String, title: String) {
        // Will be implemented in platform-specific code
    }
    
    override suspend fun shareImage(imagePath: String, text: String) {
        // Will be implemented in platform-specific code
    }
    
    override suspend fun copyToClipboard(text: String): Boolean {
        // Will be implemented in platform-specific code
        return false
    }
} 