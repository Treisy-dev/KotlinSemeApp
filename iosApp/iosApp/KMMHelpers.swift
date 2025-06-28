import shared

// Helper functions to access KMM ViewModels using Koin DI
func getChatViewModel() -> ChatViewModel {
    return try! ExportedFunctions().createChatViewModel()
}

func getPromptViewModel() -> PromptViewModel {
    return try! ExportedFunctions().createPromptViewModel()
}

func getHistoryViewModel() -> HistoryViewModel {
    return try! ExportedFunctions().createHistoryViewModel()
}

func getSettingsViewModel() -> SettingsViewModel {
    return try! ExportedFunctions().createSettingsViewModel()
} 
