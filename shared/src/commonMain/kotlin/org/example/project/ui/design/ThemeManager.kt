package org.example.project.ui.design

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeManager {
    private val _themeMode = MutableStateFlow("system")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()
    
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()
    
    private var _isSystemDark = false
    
    fun setThemeMode(mode: String) {
        _themeMode.value = mode
        updateDarkMode()
    }
    
    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }
    
    fun updateSystemDarkMode(isSystemDark: Boolean) {
        _isSystemDark = isSystemDark
        updateDarkMode()
    }
    
    private fun updateDarkMode() {
        val shouldBeDark = when (_themeMode.value) {
            "light" -> false
            "dark" -> true
            "system" -> _isSystemDark
            else -> false
        }
        _isDarkMode.value = shouldBeDark
    }
    
    fun getCurrentThemeMode(): String = _themeMode.value
    
    fun getCurrentIsDark(): Boolean = _isDarkMode.value
}

object ThemeManagerProvider {
    private var instance: ThemeManager? = null
    
    fun getInstance(): ThemeManager {
        if (instance == null) {
            instance = ThemeManager()
        }
        return instance!!
    }
} 