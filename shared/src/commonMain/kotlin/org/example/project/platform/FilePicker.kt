package org.example.project.platform

expect class FilePicker {
    suspend fun pickImage(): String?
    suspend fun pickFile(extensions: List<String>): String?
} 