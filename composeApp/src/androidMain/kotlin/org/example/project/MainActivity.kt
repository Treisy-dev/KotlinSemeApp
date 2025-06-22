package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.example.project.di.initKoin
import org.example.project.di.androidModule
import org.example.project.ui.design.AppTheme
import org.example.project.ui.design.LocalTheme
import org.example.project.ui.design.darkScheme
import org.example.project.ui.design.lightScheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Koin with Android-specific module
        initKoin(androidModule)
        
        setContent {
            val appTheme by remember { mutableStateOf(AppTheme("system", false)) }
            
            CompositionLocalProvider(LocalTheme provides appTheme) {
                MaterialTheme(
                    colorScheme = if (LocalTheme.current.isDark) darkScheme else lightScheme
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val items = listOf(
                    NavigationItem(
                        route = "chat",
                        title = "Chat",
                        icon = Icons.Default.Message
                    ),
                    NavigationItem(
                        route = "history",
                        title = "History", 
                        icon = Icons.Default.History
                    ),
                    NavigationItem(
                        route = "prompt",
                        title = "New Chat",
                        icon = Icons.Default.Add
                    ),
                    NavigationItem(
                        route = "settings",
                        title = "Settings",
                        icon = Icons.Default.Settings
                    )
                )
                
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "chat",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("chat") {
                ChatScreen()
            }
            composable("history") {
                HistoryScreen()
            }
            composable("prompt") {
                PromptScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)