import SwiftUI
import shared

class ThemeManager: ObservableObject {
    static let shared = ThemeManager()
    
    @Published var isDarkMode: Bool = false
    @Published var themeMode: String = "system"
    @Published var isSystemDark: Bool = false
    
    private let settingsViewModel: SettingsViewModel
    
    private init() {
        self.settingsViewModel = getSettingsViewModel()
        setupThemeObserver()
        setupSystemThemeObserver()
    }
    
    private func setupThemeObserver() {
        // Observe theme changes from KMM
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            guard let state = self.settingsViewModel.state.value as? SettingsState else { return }
            
            DispatchQueue.main.async {
                let shouldUpdate = self.isDarkMode != state.isDarkMode || self.themeMode != state.themeMode
                if shouldUpdate {
                    self.isDarkMode = state.isDarkMode
                    self.themeMode = state.themeMode
                    print("🎨 Theme updated: isDarkMode=\(self.isDarkMode), themeMode=\(self.themeMode)")
                }
            }
        }
    }
    
    private func setupSystemThemeObserver() {
        // Observe system theme changes
        NotificationCenter.default.addObserver(
            forName: NSNotification.Name("NSUserDefaultsDidChangeNotification"),
            object: nil,
            queue: .main
        ) { [weak self] _ in
            self?.updateSystemTheme()
        }
    }
    
    func updateSystemTheme() {
        let isDark = UITraitCollection.current.userInterfaceStyle == .dark
        if isSystemDark != isDark {
            isSystemDark = isDark
            print("🌙 System theme changed: isDark=\(isDark)")
        }
    }
    
    func getCurrentTheme() -> ColorScheme? {
        switch themeMode {
        case "light":
            return .light
        case "dark":
            return .dark
        case "system":
            return nil // Use system default
        default:
            return nil
        }
    }
    
    func shouldUseDarkMode() -> Bool {
        switch themeMode {
        case "light":
            return false
        case "dark":
            return true
        case "system":
            return isSystemDark
        default:
            return isSystemDark
        }
    }
    
    func setDarkMode(_ enabled: Bool) {
        settingsViewModel.handleEvent(event: SettingsEvent.SetDarkMode(enabled: enabled))
    }
    
    func setThemeMode(_ mode: String) {
        settingsViewModel.handleEvent(event: SettingsEvent.SetThemeMode(mode: mode))
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
} 