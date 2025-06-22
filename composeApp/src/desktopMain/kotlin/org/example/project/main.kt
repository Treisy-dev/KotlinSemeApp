package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.project.di.initKoin
import org.example.project.di.desktopModule
import org.example.project.data.local.SettingsStorage
import org.example.project.ui.design.ThemeManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.koin.core.context.GlobalContext

fun main() = application {
    println("Starting SemeApp Desktop...")
    
    // Initialize Koin with desktop-specific module
    println("Initializing Koin with desktop module...")
    initKoin(desktopModule)
    println("Koin initialized successfully")

    // Get Koin instance
    val koin = GlobalContext.get()
    val settingsStorage: SettingsStorage = koin.get()
    val themeManager: ThemeManager = koin.get()
    val themeMode: String = runBlocking { settingsStorage.getThemeMode().first() }
    val isDark: Boolean = runBlocking { settingsStorage.isDarkMode().first() }
    themeManager.setThemeMode(themeMode)
    themeManager.setDarkMode(isDark)

    Window(
        onCloseRequest = ::exitApplication,
        title = "SemeApp - AI Chat",
        state = rememberWindowState()
    ) {
        println("Creating AppDesktop...")
        AppDesktop()
    }
}