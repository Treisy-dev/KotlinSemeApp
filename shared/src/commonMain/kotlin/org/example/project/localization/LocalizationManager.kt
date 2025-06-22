package org.example.project.localization

import org.example.project.platform.getPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalizationManager {
    private val platform = getPlatform()
    
    private val _currentLanguage = MutableStateFlow(platform.getLanguage())
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()
    
    fun setLanguage(language: String) {
        platform.setLanguage(language)
        _currentLanguage.value = language
    }
    
    fun getCurrentLanguage(): String = _currentLanguage.value
    
    fun getString(key: String): String {
        return Strings.get(key, _currentLanguage.value)
    }
    
    fun getString(key: String, vararg args: Any): String {
        val baseString = Strings.get(key, _currentLanguage.value)
        return if (args.isNotEmpty()) {
            try {
                String.format(baseString, *args)
            } catch (e: Exception) {
                baseString
            }
        } else {
            baseString
        }
    }
}

object LocalizationManagerProvider {
    private var instance: LocalizationManager? = null
    
    fun getInstance(): LocalizationManager {
        if (instance == null) {
            instance = LocalizationManager()
        }
        return instance!!
    }
} 