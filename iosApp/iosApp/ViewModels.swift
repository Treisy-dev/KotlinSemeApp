import SwiftUI
import shared
import Combine

// MARK: - ChatViewModelWrapper
class ChatViewModelWrapper: ObservableObject {
    private let kmmViewModel: ChatViewModel
    private var cancellables = Set<AnyCancellable>()
    private var timer: Timer?
    
    @Published var messages: [ChatMessage] = []
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var currentMessage: String = ""
    
    init() {
        kmmViewModel = getChatViewModel()
        observeState()
    }
    
    private func observeState() {
        // Use a timer to periodically check the state
        timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            guard let state = self.kmmViewModel.state.value as? ChatState else { return }
            DispatchQueue.main.async {
                self.messages = state.messages
                self.isLoading = state.isLoading
                
                // Handle error logging
                if let error = state.error, error != self.error {
                    self.error = error
                    ErrorHandler.shared.logCustomError(
                        message: error,
                        context: "chat_view_model",
                        additionalInfo: [
                            "messages_count": state.messages.count,
                            "is_loading": state.isLoading
                        ]
                    )
                }
                
                self.currentMessage = state.currentMessage
            }
        }
    }
    
    deinit {
        timer?.invalidate()
    }
    
    func sendMessage(_ text: String) {
        kmmViewModel.handleEvent(event: ChatEvent.UpdateMessage(text: text))
        kmmViewModel.handleEvent(event: ChatEvent.SendMessage(content: text))
    }
    
    func loadMessages(sessionId: String? = nil) {
        if let sessionId = sessionId {
            kmmViewModel.handleEvent(event: ChatEvent.LoadSession(sessionId: sessionId))
        }
    }
}

// MARK: - PromptViewModelWrapper
class PromptViewModelWrapper: ObservableObject {
    private let kmmViewModel: PromptViewModel
    private var cancellables = Set<AnyCancellable>()
    private var timer: Timer?
    
    @Published var prompt: String = ""
    @Published var imagePath: String? = nil
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    
    init() {
        kmmViewModel = getPromptViewModel()
        observeState()
    }
    
    private func observeState() {
        // Use a timer to periodically check the state
        timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            guard let state = self.kmmViewModel.state.value as? PromptState else { return }
            DispatchQueue.main.async {
                self.prompt = state.prompt
                self.imagePath = state.imagePath
                self.isLoading = state.isLoading
                
                // Handle error logging
                if let error = state.error, error != self.error {
                    self.error = error
                    ErrorHandler.shared.logCustomError(
                        message: error,
                        context: "prompt_view_model",
                        additionalInfo: [
                            "has_image": state.imagePath != nil,
                            "is_loading": state.isLoading
                        ]
                    )
                }
            }
        }
    }
    
    deinit {
        timer?.invalidate()
    }
    
    func sendPrompt(_ text: String, imagePath: String?) {
        kmmViewModel.handleEvent(event: PromptEvent.UpdatePrompt(text: text))
        if let imagePath = imagePath {
            kmmViewModel.handleEvent(event: PromptEvent.SelectImage(path: imagePath))
        }
        kmmViewModel.handleEvent(event: PromptEvent.SendPrompt())
    }
    
    func pickImage() {
        kmmViewModel.handleEvent(event: PromptEvent.PickImage())
    }
    
    func takePhoto() {
        kmmViewModel.handleEvent(event: PromptEvent.TakePhoto())
    }
    
    func clearImage() {
        kmmViewModel.handleEvent(event: PromptEvent.ClearImage())
    }
}

// MARK: - HistoryViewModelWrapper
class HistoryViewModelWrapper: ObservableObject {
    private let kmmViewModel: HistoryViewModel
    private var cancellables = Set<AnyCancellable>()
    private var timer: Timer?
    
    @Published var sessions: [ChatSession] = []
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var filterType: FilterType = .all
    
    init() {
        kmmViewModel = getHistoryViewModel()
        observeState()
    }
    
    private func observeState() {
        // Use a timer to periodically check the state
        timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            guard let state = self.kmmViewModel.state.value as? HistoryState else { return }
            DispatchQueue.main.async {
                self.sessions = state.sessions
                self.isLoading = state.isLoading
                
                // Handle error logging
                if let error = state.error, error != self.error {
                    self.error = error
                    ErrorHandler.shared.logCustomError(
                        message: error,
                        context: "history_view_model",
                        additionalInfo: [
                            "sessions_count": state.sessions.count,
                            "filter_type": state.filterType.name
                        ]
                    )
                }
                
                self.filterType = FilterType.fromKmm(state.filterType)
            }
        }
    }
    
    deinit {
        timer?.invalidate()
    }
    
    func loadSessions() {
        kmmViewModel.handleEvent(event: HistoryEvent.LoadSessions())
    }
    
    func openSession(_ id: String) {
        kmmViewModel.handleEvent(event: HistoryEvent.OpenSession(sessionId: id))
    }
    
    func deleteSession(_ id: String) {
        kmmViewModel.handleEvent(event: HistoryEvent.DeleteSession(sessionId: id))
    }
    
    func clearAllHistory() {
        kmmViewModel.handleEvent(event: HistoryEvent.ClearAllHistory())
    }
    
    func setFilter(_ filter: FilterType) {
        // Map local FilterType to KMM enum
        let kmmFilter: shared.FilterType
        switch filter {
        case .all: kmmFilter = shared.FilterType.all
        case .textOnly: kmmFilter = shared.FilterType.textOnly
        case .withImage: kmmFilter = shared.FilterType.withImage
        }
        kmmViewModel.handleEvent(event: HistoryEvent.SetFilter(filterType: kmmFilter))
    }
}

// MARK: - SettingsViewModelWrapper
class SettingsViewModelWrapper: ObservableObject {
    private let kmmViewModel: SettingsViewModel
    private var cancellables = Set<AnyCancellable>()
    private var timer: Timer?
    
    @Published var isDarkMode: Bool = false
    @Published var themeMode: String = "system"
    @Published var language: String = "en"
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var appVersion: String = "1.0.0"
    @Published var buildNumber: String = "1"
    
    init() {
        kmmViewModel = getSettingsViewModel()
        observeState()
    }
    
    private func observeState() {
        // Use a timer to periodically check the state
        timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            guard let state = self.kmmViewModel.state.value as? SettingsState else { return }
            DispatchQueue.main.async {
                self.isDarkMode = state.isDarkMode
                self.themeMode = state.themeMode
                self.language = state.language
                self.isLoading = state.isLoading
                
                // Handle error logging
                if let error = state.error, error != self.error {
                    self.error = error
                    ErrorHandler.shared.logCustomError(
                        message: error,
                        context: "settings_view_model",
                        additionalInfo: [
                            "theme_mode": state.themeMode,
                            "language": state.language,
                            "is_dark_mode": state.isDarkMode
                        ]
                    )
                }
            }
        }
    }
    
    deinit {
        timer?.invalidate()
    }
    
    func loadSettings() {
        kmmViewModel.handleEvent(event: SettingsEvent.LoadSettings())
    }
    
    func setDarkMode(_ enabled: Bool) {
        kmmViewModel.handleEvent(event: SettingsEvent.SetDarkMode(enabled: enabled))
    }
    
    func setThemeMode(_ mode: String) {
        kmmViewModel.handleEvent(event: SettingsEvent.SetThemeMode(mode: mode))
    }
    
    func setLanguage(_ lang: String) {
        kmmViewModel.handleEvent(event: SettingsEvent.SetLanguage(language: lang))
    }
    
    func clearAllData() {
        kmmViewModel.handleEvent(event: SettingsEvent.ClearAllData())
    }
    
    func exportData() {
        // TODO: Implement export if needed
    }
    
    func openPrivacyPolicy() {
        // TODO: Implement open privacy policy
    }
    
    func openTermsOfService() {
        // TODO: Implement open terms
    }
}

// Local enum for iOS filter
enum FilterType: String, CaseIterable {
    case all = "all"
    case textOnly = "text_only"
    case withImage = "with_image"
    
    static func fromKmm(_ kmm: shared.FilterType) -> FilterType {
        switch kmm.name.uppercased() {
        case "ALL": return .all
        case "TEXT_ONLY": return .textOnly
        case "WITH_IMAGE": return .withImage
        default: return .all
        }
    }
} 