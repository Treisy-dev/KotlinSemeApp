import shared

// Helper functions to access KMM ViewModels
func getChatViewModel() -> ChatViewModel {
    return ChatViewModel(
        chatRepository: ChatRepository(
            database: ChatDatabaseImpl(),
            geminiRepository: GeminiRepository(
                apiService: GeminiApiService(),
                imageEncoder: ImageEncoder()
            )
        ),
        settingsRepository: SettingsRepository(
            storage: SettingsStorageImpl()
        ),
        shareSheet: ShareSheetImpl()
    )
}

func getPromptViewModel() -> PromptViewModel {
    return PromptViewModel(
        chatRepository: ChatRepository(
            database: ChatDatabaseImpl(),
            geminiRepository: GeminiRepository(
                apiService: GeminiApiService(),
                imageEncoder: ImageEncoder()
            )
        ),
        platform: IOSPlatform()
    )
}

func getHistoryViewModel() -> HistoryViewModel {
    return HistoryViewModel(
        chatRepository: ChatRepository(
            database: ChatDatabaseImpl(),
            geminiRepository: GeminiRepository(
                apiService: GeminiApiService(),
                imageEncoder: ImageEncoder()
            )
        )
    )
}

func getSettingsViewModel() -> SettingsViewModel {
    return SettingsViewModel(
        settingsRepository: SettingsRepository(
            storage: SettingsStorageImpl()
        )
    )
} 
