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
            // Tab Bar
            "chat": "Chat",
            "history": "History",
            "prompts": "Prompts",
            "settings": "Settings",
            
            // Chat Screen
            "chat_title": "Chat",
            "chat_new_session": "New Chat",
            "chat_placeholder": "Type a message...",
            "chat_send": "Send",
            "chat_loading": "Sending...",
            "chat_error": "Send Error",
            "chat_attach_image": "Attach Image",
            "chat_take_photo": "Take Photo",
            "chat_typing": "AI is typing...",
            "chat_retry": "Retry",
            "chat_copy_message": "Copy message",
            "chat_message_copied": "Message copied to clipboard",
            "chat_copy_failed": "Failed to copy message",
            "chat_subtitle": "Ask Gemini anything or share an image for analysis",
            
            // History Screen
            "history_title": "History",
            "history_empty": "History is empty",
            "history_subtitle": "Start a new conversation to see it here",
            "history_filter_all": "All",
            "history_filter_text": "Text only",
            "history_filter_image": "With images",
            "history_delete": "Delete",
            "history_share": "Share",
            "history_copy": "Copy",
            "history_no_messages": "No messages in this conversation",
            
            // Prompt Screen
            "prompt_title": "New Prompt",
            "prompt_placeholder": "Describe what you want...",
            "prompt_send": "Send",
            "prompt_loading": "Sending...",
            "prompt_error": "Send Error",
            "prompt_clear": "Clear",
            "prompt_examples": "Example Prompts",
            "prompt_example_1": "Write a story about...",
            "prompt_example_2": "Explain how to...",
            "prompt_example_3": "Create a poem about...",
            "prompt_add_image": "Add Image (Optional)",
            "prompt_gallery": "Gallery",
            "prompt_camera": "Camera",
            "prompt_remove_image": "Remove Image",
            
            // Settings Screen
            "settings_title": "Settings",
            "settings_theme": "Theme",
            "settings_language": "Language",
            "settings_data_management": "Data Management",
            "settings_about": "About",
            "theme_light": "Light",
            "theme_dark": "Dark",
            "theme_system": "System",
            "theme_mode": "Theme Mode",
            "theme_dark_description": "Use dark theme",
            "language_english": "English",
            "language_russian": "Russian",
            "clear_all_data": "Clear All Data",
            "clear_all_data_description": "This will delete all chat history and settings",
            "app_name_full": "SemeApp - AI Chat",
            "app_version": "Version 1.0.0",
            "powered_by": "Powered by Google Gemini AI",
            "build": "Build",
            "privacy_policy": "Privacy Policy",
            "terms_of_service": "Terms of Service",
            
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
            // Tab Bar
            "chat": "Чат",
            "history": "История",
            "prompts": "Промпты",
            "settings": "Настройки",
            
            // Chat Screen
            "chat_title": "Чат",
            "chat_new_session": "Новый чат",
            "chat_placeholder": "Введите сообщение...",
            "chat_send": "Отправить",
            "chat_loading": "Отправка...",
            "chat_error": "Ошибка отправки",
            "chat_attach_image": "Прикрепить изображение",
            "chat_take_photo": "Сделать фото",
            "chat_typing": "AI печатает...",
            "chat_retry": "Повторить",
            "chat_copy_message": "Копировать сообщение",
            "chat_message_copied": "Сообщение скопировано в буфер обмена",
            "chat_copy_failed": "Не удалось скопировать сообщение",
            "chat_subtitle": "Спросите Gemini что угодно или поделитесь изображением для анализа",
            
            // History Screen
            "history_title": "История",
            "history_empty": "История пуста",
            "history_subtitle": "Начните новый разговор, чтобы увидеть его здесь",
            "history_filter_all": "Все",
            "history_filter_text": "Только текст",
            "history_filter_image": "С изображениями",
            "history_delete": "Удалить",
            "history_share": "Поделиться",
            "history_copy": "Копировать",
            "history_no_messages": "В этом разговоре нет сообщений",
            
            // Prompt Screen
            "prompt_title": "Новый запрос",
            "prompt_placeholder": "Опишите, что вы хотите...",
            "prompt_send": "Отправить",
            "prompt_loading": "Отправка...",
            "prompt_error": "Ошибка отправки",
            "prompt_clear": "Очистить",
            "prompt_examples": "Примеры промптов",
            "prompt_example_1": "Напиши историю о...",
            "prompt_example_2": "Объясни как...",
            "prompt_example_3": "Создай стихотворение о...",
            "prompt_add_image": "Добавить изображение (необязательно)",
            "prompt_gallery": "Галерея",
            "prompt_camera": "Камера",
            "prompt_remove_image": "Удалить изображение",
            
            // Settings Screen
            "settings_title": "Настройки",
            "settings_theme": "Тема",
            "settings_language": "Язык",
            "settings_data_management": "Управление данными",
            "settings_about": "О приложении",
            "theme_light": "Светлая",
            "theme_dark": "Тёмная",
            "theme_system": "Системная",
            "theme_mode": "Режим темы",
            "theme_dark_description": "Использовать тёмную тему",
            "language_english": "English",
            "language_russian": "Русский",
            "clear_all_data": "Очистить все данные",
            "clear_all_data_description": "Это удалит всю историю чатов и настройки",
            "app_name_full": "SemeApp - AI Чат",
            "app_version": "Версия 1.0.0",
            "powered_by": "Работает на Google Gemini AI",
            "build": "Сборка",
            "privacy_policy": "Политика конфиденциальности",
            "terms_of_service": "Условия использования",
            
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