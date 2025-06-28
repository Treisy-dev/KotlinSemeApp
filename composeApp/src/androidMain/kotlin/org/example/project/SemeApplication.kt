package org.example.project

import android.app.Application
import com.google.firebase.FirebaseApp
import org.example.project.di.createAndroidAppModule
import org.example.project.di.initKoin
import org.example.project.ui.MainActivity

class SemeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this@SemeApplication)
        initKoin(
            createAndroidAppModule(
                context = this@SemeApplication,
                cameraServiceProvider = MainActivity::provideCameraService,
                imageServiceProvider = MainActivity::provideImageService
            )
        )
    }
}
