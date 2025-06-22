import SwiftUI
import shared

struct SettingsView: View {
    @StateObject private var viewModel = SettingsViewModelWrapper()
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {
                    // Appearance Section
                    SettingsSection(title: "Appearance") {
                        VStack(spacing: 16) {
                            // Dark Mode Toggle
                            HStack {
                                HStack {
                                    Image(systemName: "moon.fill")
                                        .foregroundColor(.blue)
                                    VStack(alignment: .leading) {
                                        Text("Dark Mode")
                                            .font(.body)
                                        Text("Use dark theme")
                                            .font(.caption)
                                            .foregroundColor(.secondary)
                                    }
                                }
                                Spacer()
                                Toggle("", isOn: Binding(
                                    get: { viewModel.isDarkMode },
                                    set: { viewModel.setDarkMode($0) }
                                ))
                            }
                            
                            Divider()
                            
                            // Theme Mode Selection
                            VStack(alignment: .leading, spacing: 8) {
                                Text("Theme Mode")
                                    .font(.body)
                                
                                HStack(spacing: 8) {
                                    ThemeChip(
                                        title: "Light",
                                        isSelected: viewModel.themeMode == "light",
                                        action: { viewModel.setThemeMode("light") }
                                    )
                                    
                                    ThemeChip(
                                        title: "Dark",
                                        isSelected: viewModel.themeMode == "dark",
                                        action: { viewModel.setThemeMode("dark") }
                                    )
                                    
                                    ThemeChip(
                                        title: "System",
                                        isSelected: viewModel.themeMode == "system",
                                        action: { viewModel.setThemeMode("system") }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Language Section
                    SettingsSection(title: "Language") {
                        HStack(spacing: 8) {
                            LanguageChip(
                                title: "English",
                                isSelected: viewModel.language == "en",
                                action: { viewModel.setLanguage("en") }
                            )
                            
                            LanguageChip(
                                title: "Русский",
                                isSelected: viewModel.language == "ru",
                                action: { viewModel.setLanguage("ru") }
                            )
                        }
                    }
                    
                    // Data Management Section
                    SettingsSection(title: "Data Management") {
                        VStack(spacing: 12) {
                            Button(action: { viewModel.clearAllData() }) {
                                HStack {
                                    Image(systemName: "trash.fill")
                                        .foregroundColor(.red)
                                    Text("Clear All Data")
                                        .foregroundColor(.red)
                                    Spacer()
                                }
                                .padding()
                                .background(Color(.systemRed).opacity(0.1))
                                .cornerRadius(10)
                            }
                            
                            Button(action: { viewModel.exportData() }) {
                                HStack {
                                    Image(systemName: "square.and.arrow.up")
                                        .foregroundColor(.blue)
                                    Text("Export Data")
                                        .foregroundColor(.blue)
                                    Spacer()
                                }
                                .padding()
                                .background(Color(.systemBlue).opacity(0.1))
                                .cornerRadius(10)
                            }
                        }
                    }
                    
                    // About Section
                    SettingsSection(title: "About") {
                        VStack(spacing: 12) {
                            HStack {
                                Text("Version")
                                Spacer()
                                Text(viewModel.appVersion)
                                    .foregroundColor(.secondary)
                            }
                            
                            Divider()
                            
                            HStack {
                                Text("Build")
                                Spacer()
                                Text(viewModel.buildNumber)
                                    .foregroundColor(.secondary)
                            }
                            
                            Divider()
                            
                            Button(action: { viewModel.openPrivacyPolicy() }) {
                                HStack {
                                    Text("Privacy Policy")
                                    Spacer()
                                    Image(systemName: "arrow.up.right.square")
                                }
                                .foregroundColor(.blue)
                            }
                            
                            Divider()
                            
                            Button(action: { viewModel.openTermsOfService() }) {
                                HStack {
                                    Text("Terms of Service")
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
            .navigationTitle("Settings")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Done") {
                        dismiss()
                    }
                }
            }
        }
        .onAppear {
            viewModel.loadSettings()
        }
    }
}

struct SettingsSection<Content: View>: View {
    let title: String
    let content: Content
    
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
        .background(Color(.systemGray6))
        .cornerRadius(12)
    }
}

struct ThemeChip: View {
    let title: String
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.caption)
                .padding(.horizontal, 12)
                .padding(.vertical, 6)
                .background(isSelected ? Color.blue : Color(.systemGray5))
                .foregroundColor(isSelected ? .white : .primary)
                .cornerRadius(16)
        }
    }
}

struct LanguageChip: View {
    let title: String
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.body)
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .background(isSelected ? Color.blue : Color(.systemGray5))
                .foregroundColor(isSelected ? .white : .primary)
                .cornerRadius(20)
        }
    }
}

#Preview {
    SettingsView()
} 