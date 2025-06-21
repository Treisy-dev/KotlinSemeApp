package org.example.project

import android.app.Application
import com.google.firebase.FirebaseApp
import org.example.project.di.initKoin
import org.koin.dsl.module

class SemeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initKoin(module { })
    }
} 