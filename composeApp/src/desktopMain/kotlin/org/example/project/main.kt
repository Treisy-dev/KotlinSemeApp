package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.project.di.initKoin
import org.example.project.di.desktopModule

fun main() = application {
    println("Starting SemeApp Desktop...")
    
    // Initialize Koin with desktop-specific module
    println("Initializing Koin with desktop module...")
    initKoin(desktopModule)
    println("Koin initialized successfully")
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "SemeApp - AI Chat",
        state = rememberWindowState()
    ) {
        println("Creating AppDesktop...")
        AppDesktop()
    }
}