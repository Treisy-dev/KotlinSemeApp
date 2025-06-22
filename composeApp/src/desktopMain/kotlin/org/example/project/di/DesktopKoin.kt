package org.example.project.di

import org.example.project.platform.DesktopPlatform
import org.example.project.share.DesktopShareSheet
import org.example.project.platform.Platform
import org.example.project.platform.FilePicker
import org.example.project.share.ShareSheet
import org.example.project.data.local.ChatDatabase
import org.example.project.data.local.DesktopDatabase
import org.koin.dsl.module

val desktopModule = module {
    // Desktop-specific implementations
    single<Platform> { 
        println("Initializing DesktopPlatform")
        DesktopPlatform() 
    }
    single<ShareSheet> { 
        println("Initializing DesktopShareSheet")
        DesktopShareSheet() 
    }
    single<FilePicker> { 
        println("Initializing FilePicker")
        FilePicker() 
    }
    single<ChatDatabase> { 
        println("Initializing DesktopDatabase")
        DesktopDatabase() 
    }
} 