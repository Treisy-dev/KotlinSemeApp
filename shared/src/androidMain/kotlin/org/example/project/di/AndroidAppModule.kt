package org.example.project.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import org.example.project.data.di.dataModule
import org.example.project.data.firebase.AndroidFirebaseAnalytics
import org.example.project.data.local.AndroidChatDatabase
import org.example.project.data.local.AndroidSettingsStorage
import org.example.project.data.local.ChatDatabase
import org.example.project.data.local.SettingsStorage
import org.example.project.firebase.FirebaseAnalytics
import org.example.project.platform.AndroidPlatform
import org.example.project.platform.Platform
import org.example.project.platform.contract.CameraService
import org.example.project.platform.contract.ImageService
import org.example.project.platform.utils.AndroidFileResolver
import org.example.project.share.AndroidShareSheet
import org.example.project.share.ShareSheet
import org.koin.core.module.Module
import org.koin.dsl.module

fun createAndroidAppModule(
    context: Context,
    cameraServiceProvider: () -> CameraService?,
    imageServiceProvider: () -> ImageService?
): Module = module {
    single { context }
    includes(dataModule)
    single<FirebaseAnalytics> {
        AndroidFirebaseAnalytics(Firebase.analytics)
    }
    single { AndroidFileResolver(get()) }
    single<Platform> {
        AndroidPlatform(
            context, cameraServiceProvider, imageServiceProvider,
            get(), get(), get(), get()
        )
    }
    single<ShareSheet> {
        AndroidShareSheet(get())
    }
    single<ChatDatabase> {
        AndroidChatDatabase(get(), get())
    }
    single<SettingsStorage> {
        AndroidSettingsStorage()
    }
}
