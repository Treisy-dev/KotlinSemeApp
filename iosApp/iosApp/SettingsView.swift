import SwiftUI
import shared

struct SettingsView: View {
    @StateObject private var viewModel = SettingsViewModelWrapper()
    @StateObject private var themeManager = ThemeManager.shared
    @StateObject private var localizationManager = LocalizationManager.shared
    @Environment(\.dismiss) private var dismiss
    @Environment(\.colorScheme) private var colorScheme
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {
                    // Appearance Section
                    SettingsSection(title: localizationManager.getString("settings_theme")) {
                        VStack(spacing: 16) {
                            // Dark Mode Toggle
                            HStack {
                                HStack {
                                    Image(systemName: "moon.fill")
                                        .foregroundColor(.blue)
                                    VStack(alignment: .leading) {
                                        Text(localizationManager.getString("theme_dark"))
                                            .font(.body)
                                        Text(localizationManager.getString("theme_dark_description"))
                                            .font(.caption)
                                            .foregroundColor(.secondary)
                                    }
                                }
                                Spacer()
                                Toggle("", isOn: Binding(
                                    get: { themeManager.isDarkMode },
                                    set: { 
                                        themeManager.setDarkMode($0)
                                        FirebaseAnalyticsManager.shared.trackSettingsChanged(settingName: "dark_mode", newValue: $0 ? "enabled" : "disabled")
                                    }
                                ))
                            }
                            
                            Divider()
                            
                            // Theme Mode Selection
                            VStack(alignment: .leading, spacing: 8) {
                                Text(localizationManager.getString("theme_mode"))
                                    .font(.body)
                                
                                HStack(spacing: 8) {
                                    ThemeChip(
                                        title: localizationManager.getString("theme_light"),
                                        isSelected: themeManager.themeMode == "light",
                                        action: { 
                                            themeManager.setThemeMode("light")
                                            FirebaseAnalyticsManager.shared.trackSettingsChanged(settingName: "theme_mode", newValue: "light")
                                        }
                                    )
                                    
                                    ThemeChip(
                                        title: localizationManager.getString("theme_dark"),
                                        isSelected: themeManager.themeMode == "dark",
                                        action: { 
                                            themeManager.setThemeMode("dark")
                                            FirebaseAnalyticsManager.shared.trackSettingsChanged(settingName: "theme_mode", newValue: "dark")
                                        }
                                    )
                                    
                                    ThemeChip(
                                        title: localizationManager.getString("theme_system"),
                                        isSelected: themeManager.themeMode == "system",
                                        action: { 
                                            themeManager.setThemeMode("system")
                                            FirebaseAnalyticsManager.shared.trackSettingsChanged(settingName: "theme_mode", newValue: "system")
                                        }
                                    )
                                    Spacer()
                                }
                            }
                        }
                    }
                    
                    // Language Section
                    SettingsSection(title: localizationManager.getString("settings_language")) {
                        HStack(spacing: 8) {
                            LanguageChip(
                                title: localizationManager.getString("language_english"),
                                isSelected: viewModel.language == "en",
                                action: { 
                                    viewModel.setLanguage("en")
                                    FirebaseAnalyticsManager.shared.trackSettingsChanged(settingName: "language", newValue: "en")
                                }
                            )
                            
                            LanguageChip(
                                title: localizationManager.getString("language_russian"),
                                isSelected: viewModel.language == "ru",
                                action: { 
                                    viewModel.setLanguage("ru")
                                    FirebaseAnalyticsManager.shared.trackSettingsChanged(settingName: "language", newValue: "ru")
                                }
                            )
                            
                            Spacer()
                        }
                    }
                    
                    // Data Management Section
                    SettingsSection(title: localizationManager.getString("settings_data_management")) {
                        VStack(spacing: 12) {
                            Button(action: { 
                                viewModel.clearAllData()
                                FirebaseAnalyticsManager.shared.trackCustomEvent(name: "data_cleared")
                            }) {
                                HStack {
                                    Image(systemName: "trash.fill")
                                        .foregroundColor(.red)
                                    Text(localizationManager.getString("clear_all_data"))
                                        .foregroundColor(.red)
                                    Spacer()
                                }
                                .padding()
                                .background(Color(.systemRed).opacity(0.1))
                                .cornerRadius(10)
                            }
                        }
                    }
                    
                    // About Section
                    SettingsSection(title: localizationManager.getString("settings_about")) {
                        VStack(spacing: 12) {
                            HStack {
                                Text(localizationManager.getString("app_version"))
                                Spacer()
                                Text(viewModel.appVersion)
                                    .foregroundColor(.secondary)
                            }
                            
                            Divider()
                            
                            HStack {
                                Text(localizationManager.getString("build"))
                                Spacer()
                                Text(viewModel.buildNumber)
                                    .foregroundColor(.secondary)
                            }
                            
                            Divider()
                            
                            Button(action: { 
                                viewModel.openPrivacyPolicy()
                                FirebaseAnalyticsManager.shared.trackCustomEvent(name: "privacy_policy_opened")
                            }) {
                                HStack {
                                    Text(localizationManager.getString("privacy_policy"))
                                    Spacer()
                                    Image(systemName: "arrow.up.right.square")
                                }
                                .foregroundColor(.blue)
                            }
                            
                            Divider()
                            
                            Button(action: { 
                                viewModel.openTermsOfService()
                                FirebaseAnalyticsManager.shared.trackCustomEvent(name: "terms_of_service_opened")
                            }) {
                                HStack {
                                    Text(localizationManager.getString("terms_of_service"))
                                    Spacer()
                                    Image(systemName: "arrow.up.right.square")
                                }
                                .foregroundColor(.blue)
                            }
                        }
                    }
                }
                .padding()
            }
            .navigationTitle(localizationManager.getString("settings_title"))
            .navigationBarTitleDisplayMode(.inline)
        }
        .onAppear {
            viewModel.loadSettings()
            FirebaseAnalyticsManager.shared.trackScreenView(screenName: FirebaseAnalyticsManager.ScreenNames.settings)
        }
    }
}

struct SettingsSection<Content: View>: View {
    let title: String
    let content: Content
    @Environment(\.colorScheme) private var colorScheme
    
    init(title: String, @ViewBuilder content: () -> Content) {
        self.title = title
        self.content = content()
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(title)
                .font(.title2)
                .fontWeight(.medium)
            
            content
        }
        .padding()
        .background(colorScheme == .dark ? Color(.systemGray6) : Color(.systemGray6))
        .cornerRadius(12)
    }
}

struct ThemeChip: View {
    let title: String
    let isSelected: Bool
    let action: () -> Void
    @Environment(\.colorScheme) private var colorScheme
    
    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.caption)
                .padding(.horizontal, 12)
                .padding(.vertical, 6)
                .background(isSelected ? Color.blue : (colorScheme == .dark ? Color(.systemGray5) : Color(.systemGray5)))
                .foregroundColor(isSelected ? .white : .primary)
                .cornerRadius(16)
        }
    }
}

struct LanguageChip: View {
    let title: String
    let isSelected: Bool
    let action: () -> Void
    @Environment(\.colorScheme) private var colorScheme
    
    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.caption)
                .padding(.horizontal, 12)
                .padding(.vertical, 6)
                .background(isSelected ? Color.blue : (colorScheme == .dark ? Color(.systemGray5) : Color(.systemGray5)))
                .foregroundColor(isSelected ? .white : .primary)
                .cornerRadius(16)
        }
    }
}

#Preview {
    SettingsView()
} 
