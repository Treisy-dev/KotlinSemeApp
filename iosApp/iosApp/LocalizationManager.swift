import Foundation
import shared

class LocalizationManager: ObservableObject {
    static let shared = LocalizationManager()
    
    @Published var currentLanguage: String = "en"
    
    private let settingsViewModel: SettingsViewModel
    
    private init() {
        self.settingsViewModel = getSettingsViewModel()
        setupLanguageObserver()
    }
    
    private func setupLanguageObserver() {
        // Observe language changes from KMM
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            guard let state = self.settingsViewModel.state.value as? SettingsState else { return }
            
            DispatchQueue.main.async {
                if self.currentLanguage != state.language {
                    self.currentLanguage = state.language
                    print("🌍 Language updated: \(self.currentLanguage)")
                }
            }
        }
    }
    
    func getString(_ key: String) -> String {
        return Strings.get(key: key, language: currentLanguage)
    }
    
    func setLanguage(_ language: String) {
        settingsViewModel.handleEvent(event: SettingsEvent.SetLanguage(language: language))
    }
}

// Localization strings
struct Strings {
    private static let strings: [String: [String: String]] = [
        "en": [
            // Settings Screen
            "settings_title": "Settings",
            "settings_theme": "Theme",
            "settings_language": "Language",
            "settings_data_management": "Data Management",
            "settings_about": "About",
            "theme_light": "Light",
            "theme_dark": "Dark",
            "theme_system": "System",
            "language_english": "English",
            "language_russian": "Русский",
            "clear_all_data": "Clear All Data",
            "clear_all_data_description": "This will delete all chat history and settings",
            "app_name_full": "SemeApp - AI Chat",
            "app_version": "Version 1.0.0",
            "powered_by": "Powered by Google Gemini AI",
            
            // Navigation
            "nav_chat": "Chat",
            "nav_history": "History",
            "nav_prompts": "Prompts",
            "nav_settings": "Settings",
            
            // Chat Screen
            "chat_placeholder": "Type your message...",
            "chat_send": "Send",
            "chat_attach_image": "Attach Image",
            "chat_take_photo": "Take Photo",
            "chat_typing": "AI is typing...",
            "chat_error": "Error sending message",
            "chat_retry": "Retry",
            "chat_new_session": "New Chat",
            "chat_copy_message": "Copy message",
            "chat_message_copied": "Message copied to clipboard",
            "chat_copy_failed": "Failed to copy message",
            
            // History Screen
            "history_title": "Chat History",
            "history_empty": "No chat history yet",
            "history_filter_all": "All",
            "history_filter_text": "Text Only",
            "history_filter_image": "With Image",
            "history_delete": "Delete",
            "history_share": "Share",
            
            // Prompt Screen
            "prompt_title": "Prompts",
            "prompt_placeholder": "Enter your prompt...",
            "prompt_send": "Send Prompt",
            "prompt_clear": "Clear",
            "prompt_examples": "Example Prompts",
            "prompt_example_1": "Write a story about...",
            "prompt_example_2": "Explain how to...",
            "prompt_example_3": "Create a poem about...",
            
            // Common
            "error": "Error",
            "success": "Success",
            "loading": "Loading...",
            "cancel": "Cancel",
            "ok": "OK",
            "yes": "Yes",
            "no": "No",
            "delete": "Delete",
            "share": "Share",
            "copy": "Copy",
            "paste": "Paste",
            "select": "Select",
            "save": "Save",
            "edit": "Edit",
            "close": "Close",
            "back": "Back",
            "next": "Next",
            "previous": "Previous",
            "done": "Done",
            "continue": "Continue"
        ],
        "ru": [
            // Settings Screen
            "settings_title": "Настройки",
            "settings_theme": "Тема",
            "settings_language": "Язык",
            "settings_data_management": "Управление данными",
            "settings_about": "О приложении",
            "theme_light": "Светлая",
            "theme_dark": "Тёмная",
            "theme_system": "Системная",
            "language_english": "English",
            "language_russian": "Русский",
            "clear_all_data": "Очистить все данные",
            "clear_all_data_description": "Это удалит всю историю чатов и настройки",
            "app_name_full": "SemeApp - AI Чат",
            "app_version": "Версия 1.0.0",
            "powered_by": "Работает на Google Gemini AI",
            
            // Navigation
            "nav_chat": "Чат",
            "nav_history": "История",
            "nav_prompts": "Промпты",
            "nav_settings": "Настройки",
            
            // Chat Screen
            "chat_placeholder": "Введите сообщение...",
            "chat_send": "Отправить",
            "chat_attach_image": "Прикрепить изображение",
            "chat_take_photo": "Сделать фото",
            "chat_typing": "AI печатает...",
            "chat_error": "Ошибка отправки сообщения",
            "chat_retry": "Повторить",
            "chat_new_session": "Новый чат",
            "chat_copy_message": "Копировать сообщение",
            "chat_message_copied": "Сообщение скопировано в буфер обмена",
            "chat_copy_failed": "Не удалось скопировать сообщение",
            
            // History Screen
            "history_title": "История чатов",
            "history_empty": "История чатов пуста",
            "history_filter_all": "Все",
            "history_filter_text": "Только текст",
            "history_filter_image": "С изображениями",
            "history_delete": "Удалить",
            "history_share": "Поделиться",
            
            // Prompt Screen
            "prompt_title": "Промпты",
            "prompt_placeholder": "Введите промпт...",
            "prompt_send": "Отправить промпт",
            "prompt_clear": "Очистить",
            "prompt_examples": "Примеры промптов",
            "prompt_example_1": "Напиши историю о...",
            "prompt_example_2": "Объясни как...",
            "prompt_example_3": "Создай стихотворение о...",
            
            // Common
            "error": "Ошибка",
            "success": "Успешно",
            "loading": "Загрузка...",
            "cancel": "Отмена",
            "ok": "ОК",
            "yes": "Да",
            "no": "Нет",
            "delete": "Удалить",
            "share": "Поделиться",
            "copy": "Копировать",
            "paste": "Вставить",
            "select": "Выбрать",
            "save": "Сохранить",
            "edit": "Редактировать",
            "close": "Закрыть",
            "back": "Назад",
            "next": "Далее",
            "previous": "Предыдущий",
            "done": "Готово",
            "continue": "Продолжить"
        ]
    ]
    
    static func get(key: String, language: String = "en") -> String {
        return strings[language]?[key] ?? strings["en"]?[key] ?? key
    }
} 