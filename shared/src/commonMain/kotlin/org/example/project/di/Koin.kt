package org.example.project.di

import org.example.project.screens.ChatViewModel
import org.example.project.screens.SettingsViewModel
import org.example.project.screens.PromptViewModel
import org.example.project.screens.HistoryViewModel
import org.example.project.data.repository.ChatRepository
import org.example.project.data.repository.SettingsRepository
import org.example.project.data.repository.GeminiRepository
import org.example.project.data.local.ChatDatabase
import org.example.project.data.local.ChatDatabaseImpl
import org.example.project.data.local.SettingsStorage
import org.example.project.data.local.SettingsStorageImpl
import org.example.project.data.remote.GeminiApiService
import org.example.project.platform.Platform
import org.example.project.platform.PlatformImpl
import org.example.project.platform.ImageEncoder
import org.example.project.share.ShareSheet
import org.example.project.share.ShareSheetImpl
import org.example.project.localization.LocalizationManager
import org.example.project.localization.LocalizationManagerProvider
import org.example.project.ui.design.ThemeManager
import org.example.project.ui.design.ThemeManagerProvider
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

fun initKoin(appModule: Module) {
    startKoin {
        modules(
            appModule,
            sharedModule,
        )
    }
}

val sharedModule: Module = module {
    // Platform-specific implementations (will be provided by platform modules)
    // single<Platform> { PlatformImpl() } // Убрано - будет предоставлено платформенными модулями
    // single<ShareSheet> { ShareSheetImpl() } // Убрано - будет предоставлено платформенными модулями
    single { ImageEncoder() }
    
    // Localization and Theme
    single { LocalizationManagerProvider.getInstance() }
    single { ThemeManagerProvider.getInstance() }
    
    // Data layer
    single { GeminiApiService() }
    // ChatDatabase and SettingsStorage will be provided by platform-specific modules
    
    // Repositories
    single { ChatRepository(get(), get()) }
    single { SettingsRepository(get()) }
    single { GeminiRepository(get(), get()) }
    
    // ViewModels
    single { ChatViewModel(get(), get(), get()) }
    single { SettingsViewModel(get()) }
    single { PromptViewModel(get(), get()) }
    single { HistoryViewModel(get()) }
} 