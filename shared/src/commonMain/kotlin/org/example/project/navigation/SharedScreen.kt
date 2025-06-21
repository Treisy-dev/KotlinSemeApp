package org.example.project.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.screens.ChatScreen
import org.example.project.screens.SettingsScreen
import org.example.project.screens.PromptScreen
import org.example.project.screens.HistoryScreen

sealed interface SharedScreen : Screen {
    data object Chat : SharedScreen {
        @Composable
        override fun Content() {
            ChatScreen()
        }
    }
    data object Settings : SharedScreen {
        @Composable
        override fun Content() {
            SettingsScreen()
        }
    }
    data object Prompt : SharedScreen {
        @Composable
        override fun Content() {
            PromptScreen()
        }
    }
    data object History : SharedScreen {
        @Composable
        override fun Content() {
            HistoryScreen()
        }
    }
} 