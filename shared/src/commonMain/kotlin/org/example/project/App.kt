package org.example.project

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.example.project.localization.LocalLocalizationManager
import org.example.project.localization.LocalizationManagerProvider
import org.example.project.screens.ChatScreen
import org.example.project.screens.HistoryScreen
import org.example.project.screens.PromptScreen
import org.example.project.screens.SettingsScreen
import org.example.project.screens.SettingsViewModel
import org.example.project.ui.design.ThemeManager
import org.example.project.ui.design.darkScheme
import org.example.project.ui.design.lightScheme
import org.koin.compose.koinInject

@Composable
fun App() {
    val settingsViewModel = koinInject<SettingsViewModel>()
    val themeManager = koinInject<ThemeManager>()
    val localizationManager = remember { LocalizationManagerProvider.getInstance() }
    val state by settingsViewModel.state.collectAsState()
    val isSystemDark = isSystemInDarkTheme()
    val isDarkMode by themeManager.isDarkMode.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var currentSessionId by remember { mutableStateOf<String?>(null) }

    // Обновляем только системную тему
    LaunchedEffect(isSystemDark) {
        themeManager.updateSystemDarkMode(isSystemDark)
    }

    CompositionLocalProvider(LocalLocalizationManager provides localizationManager) {
        if (state.isLoading) {
            // Показываем индикатор загрузки, пока не загружены настройки
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            MaterialTheme(
                colorScheme = if (isDarkMode) darkScheme else lightScheme
            ) {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val tabs = listOf(
                                TabItem(localizationManager.getString("nav_chat"), Icons.Default.Chat, 0),
                                TabItem(localizationManager.getString("nav_history"), Icons.Default.History, 1),
                                TabItem(localizationManager.getString("nav_prompts"), Icons.Default.Edit, 2),
                                TabItem(localizationManager.getString("nav_settings"), Icons.Default.Settings, 3)
                            )
                            tabs.forEach { tab ->
                                NavigationBarItem(
                                    icon = { Icon(tab.icon, contentDescription = tab.title) },
                                    label = { Text(tab.title) },
                                    selected = selectedTab == tab.index,
                                    onClick = { selectedTab = tab.index }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        when (selectedTab) {
                            0 -> ChatScreen(
                                sessionId = currentSessionId,
                                viewModel = koinInject(),
                            )
                            1 -> HistoryScreen(
                                onSessionSelected = { sessionId ->
                                    currentSessionId = sessionId
                                    selectedTab = 0
                                }
                            )
                            2 -> PromptScreen(
                                onNavigateToChat = { sessionId ->
                                    currentSessionId = sessionId
                                    selectedTab = 0
                                }
                            )
                            3 -> SettingsScreen(

                            )
                        }
                    }
                }
            }
        }
    }
}

private data class TabItem(
    val title: String,
    val icon: ImageVector,
    val index: Int
)
