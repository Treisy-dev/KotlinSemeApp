package org.example.project.localization

import androidx.compose.runtime.compositionLocalOf

val LocalLocalizationManager = compositionLocalOf<LocalizationManager> {
    error("No LocalizationManager provided")
} 