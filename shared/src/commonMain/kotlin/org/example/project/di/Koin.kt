package org.example.project.di

import org.example.project.screens.ChatViewModel
import org.example.project.screens.SettingsViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(appModule: Module) {
    startKoin {
        modules(
            appModule,
            sharedModule,
        )
    }
}

val sharedModule: Module = module {
    // Repositories
    // ViewModels
    factory { ChatViewModel() }
    factory { SettingsViewModel() }
    // UseCases
    // etc.
} 