package org.example.project.di

import android.content.Context
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import org.example.project.data.di.dataModule
import org.example.project.data.local.AndroidDatabase
import org.example.project.data.local.AndroidSettingsStorage
import org.example.project.data.local.ChatDatabase
import org.example.project.data.local.SettingsStorage
import org.example.project.data.firebase.AndroidFirebaseAnalytics
import org.example.project.firebase.FirebaseAnalytics
import org.example.project.platform.AndroidPlatform
import org.example.project.platform.Platform
import org.example.project.share.AndroidShareSheet
import org.example.project.share.ShareSheet
import org.koin.core.module.Module
import org.koin.dsl.module

fun createAndroidAppModule(
    context: Context,
    activityProvider: () -> ComponentActivity?
): Module = module {
    single { context }
    includes(dataModule)
    single<FirebaseAnalytics> {
        AndroidFirebaseAnalytics(Firebase.analytics)
    }
    single<Platform> {
        AndroidPlatform(context, activityProvider, get(), get(), get())
    }
    single<ShareSheet> {
        AndroidShareSheet(get())
    }
    single<ChatDatabase> {
        AndroidDatabase(get(), get())
    }
    single<SettingsStorage> {
        AndroidSettingsStorage()
    }
}
