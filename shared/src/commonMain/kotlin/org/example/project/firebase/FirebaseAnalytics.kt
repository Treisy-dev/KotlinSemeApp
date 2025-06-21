package org.example.project.firebase

interface FirebaseAnalytics {
    fun logEvent(eventName: String, parameters: Map<String, String> = emptyMap())
} 