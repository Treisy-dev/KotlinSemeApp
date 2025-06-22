package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.project.di.initKoin
import org.example.project.di.desktopModule

fun main() = application {
    // Initialize Koin with desktop-specific module
    initKoin(desktopModule)
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "SemeApp - AI Chat",
        state = rememberWindowState()
    ) {
        AppDesktop()
    }
}