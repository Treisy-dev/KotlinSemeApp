package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.example.project.screens.HistoryScreenRoute
import org.example.project.ui.design.AppTheme
import org.example.project.ui.design.LocalTheme
import org.example.project.ui.design.darkScheme
import org.example.project.ui.design.lightScheme

@Composable
fun App() {
    val appTheme by remember { mutableStateOf(AppTheme("system", false)) }
    
    CompositionLocalProvider(LocalTheme provides appTheme) {
        MaterialTheme(
            colorScheme = if (LocalTheme.current.isDark) darkScheme else lightScheme
        ) {
            Navigator(screen = HistoryScreenRoute()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
} 