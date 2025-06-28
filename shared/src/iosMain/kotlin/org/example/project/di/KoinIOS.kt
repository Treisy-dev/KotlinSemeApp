package org.example.project.di

import org.example.project.di.initKoin
import org.example.project.platform.IOSPlatform
import org.example.project.platform.Platform
import org.example.project.share.IOSShareSheet
import org.example.project.share.ShareSheet
import org.example.project.data.local.ChatDatabase
import org.example.project.data.local.IOSDatabase
import org.example.project.data.local.SettingsStorage
import org.example.project.data.local.IOSSettingsStorage
import org.koin.dsl.module
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Throws(Exception::class)
fun initKoin_iOS() {
    initKoin(module {
        single<Platform> { IOSPlatform() }
        single<ShareSheet> { IOSShareSheet() }
        single<ChatDatabase> { IOSDatabase() }
        single<SettingsStorage> { IOSSettingsStorage() }
    })
} 