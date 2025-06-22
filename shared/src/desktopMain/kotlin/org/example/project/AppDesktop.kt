package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.example.project.screens.*
import org.example.project.ui.design.AppTheme
import org.example.project.ui.design.LocalTheme
import org.example.project.ui.design.darkScheme
import org.example.project.ui.design.lightScheme

@Composable
fun AppDesktop() {
    val appTheme by remember { mutableStateOf(AppTheme("system", false)) }
    var selectedTab by remember { mutableStateOf(0) }
    var currentSessionId by remember { mutableStateOf<String?>(null) }
    
    CompositionLocalProvider(LocalTheme provides appTheme) {
        MaterialTheme(
            colorScheme = if (LocalTheme.current.isDark) darkScheme else lightScheme
        ) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val tabs = listOf(
                            TabItem("Chat", Icons.Default.Chat, 0),
                            TabItem("History", Icons.Default.History, 1),
                            TabItem("Prompts", Icons.Default.Edit, 2),
                            TabItem("Settings", Icons.Default.Settings, 3)
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
                        0 -> ChatScreenDesktop(sessionId = currentSessionId)
                        1 -> HistoryScreenDesktop(
                            onSessionSelected = { sessionId ->
                                currentSessionId = sessionId
                                selectedTab = 0
                            }
                        )
                        2 -> PromptScreenDesktop(
                            onNavigateToChat = { sessionId ->
                                currentSessionId = sessionId
                                selectedTab = 0
                            }
                        )
                        3 -> SettingsScreenDesktop()
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