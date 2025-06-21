package org.example.project.di

import org.koin.dsl.module

fun initKoin_iOS() {
    initKoin(module {
        // iOS-specific dependencies can be added here
    })
} 