package org.example.project.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.example.project.App

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivity = this
        setContent {
            App()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentActivity = null
    }

    companion object {

        private var currentActivity: ComponentActivity? = null

        fun provideCurrentActivity(): ComponentActivity? = currentActivity
    }
}
