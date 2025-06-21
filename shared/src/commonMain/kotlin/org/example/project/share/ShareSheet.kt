package org.example.project.share

interface ShareSheet {
    suspend fun shareText(text: String, title: String = "Share")
    suspend fun shareImage(imagePath: String, text: String = "")
    suspend fun copyToClipboard(text: String): Boolean
} 