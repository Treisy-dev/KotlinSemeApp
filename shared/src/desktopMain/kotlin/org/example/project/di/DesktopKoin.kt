package org.example.project.di

import org.example.project.platform.DesktopPlatform
import org.example.project.share.DesktopShareSheet
import org.example.project.platform.Platform
import org.example.project.share.ShareSheet
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
} 