package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.navigation.SharedScreen
import org.example.project.ui.design.AppTheme
import org.example.project.ui.design.LocalTheme
import org.example.project.ui.design.darkScheme
import org.example.project.ui.design.lightScheme

@Composable
fun App() {
    val appTheme by remember { mutableStateOf(AppTheme("default", false)) }
    CompositionLocalProvider(LocalTheme provides appTheme) {
        MaterialTheme(
            colorScheme = if (LocalTheme.current.isDark) darkScheme else lightScheme
        ) {
            Navigator(
                screen = SharedScreen.Chat,
            )
        }
    }
} 