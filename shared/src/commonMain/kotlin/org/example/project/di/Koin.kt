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
import org.example.project.data.remote.GeminiApi
import org.example.project.data.remote.GeminiApiImpl
import org.example.project.platform.Platform
import org.example.project.platform.PlatformImpl
import org.example.project.share.ShareSheet
import org.example.project.share.ShareSheetImpl
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
    // Platform-specific implementations
    single<Platform> { PlatformImpl() }
    single<ShareSheet> { ShareSheetImpl() }
    
    // Data layer
    single<GeminiApi> { GeminiApiImpl() }
    single<ChatDatabase> { ChatDatabaseImpl() }
    single<SettingsStorage> { SettingsStorageImpl() }
    
    // Repositories
    single { ChatRepository(get(), get()) }
    single { SettingsRepository(get()) }
    single { GeminiRepository(get()) }
    
    // ViewModels
    factory { ChatViewModel(get(), get(), get()) }
    factory { SettingsViewModel(get()) }
    factory { PromptViewModel(get(), get(), get()) }
    factory { HistoryViewModel(get()) }
}

val desktopModule: Module = module {
    // Desktop-specific dependencies will be added here
} 