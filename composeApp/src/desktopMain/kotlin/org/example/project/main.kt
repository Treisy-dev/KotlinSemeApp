package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.di.sharedModule
import org.koin.core.context.startKoin
import org.example.project.App

fun main() = application {
    startKoin {
        modules(sharedModule)
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "SemeApp",
    ) {
        App()
    }
}