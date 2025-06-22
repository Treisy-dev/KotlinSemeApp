package org.example.project.localization

object Strings {
    private val strings = mapOf(
        "en" to mapOf(
            // Settings Screen
            "settings_title" to "Settings",
            "settings_theme" to "Theme",
            "settings_language" to "Language",
            "settings_data_management" to "Data Management",
            "settings_about" to "About",
            "theme_light" to "Light",
            "theme_dark" to "Dark",
            "theme_system" to "System",
            "language_english" to "English",
            "language_russian" to "Русский",
            "clear_all_data" to "Clear All Data",
            "clear_all_data_description" to "This will delete all chat history and settings",
            "app_name_full" to "SemeApp - AI Chat",
            "app_version" to "Version 1.0.0",
            "powered_by" to "Powered by Google Gemini AI",
            
            // Navigation
            "nav_chat" to "Chat",
            "nav_history" to "History",
            "nav_prompts" to "Prompts",
            "nav_settings" to "Settings",
            
            // Chat Screen
            "chat_placeholder" to "Type your message...",
            "chat_send" to "Send",
            "chat_attach_image" to "Attach Image",
            "chat_take_photo" to "Take Photo",
            "chat_typing" to "AI is typing...",
            "chat_error" to "Error sending message",
            "chat_retry" to "Retry",
            "chat_new_session" to "New Chat",
            
            // History Screen
            "history_title" to "Chat History",
            "history_empty" to "No chat history yet",
            "history_filter_all" to "All",
            "history_filter_text" to "Text Only",
            "history_filter_image" to "With Image",
            "history_delete" to "Delete",
            "history_share" to "Share",
            
            // Prompt Screen
            "prompt_title" to "Prompts",
            "prompt_placeholder" to "Enter your prompt...",
            "prompt_send" to "Send Prompt",
            "prompt_clear" to "Clear",
            "prompt_examples" to "Example Prompts",
            "prompt_example_1" to "Write a story about...",
            "prompt_example_2" to "Explain how to...",
            "prompt_example_3" to "Create a poem about...",
            
            // Common
            "error" to "Error",
            "success" to "Success",
            "loading" to "Loading...",
            "cancel" to "Cancel",
            "ok" to "OK",
            "yes" to "Yes",
            "no" to "No",
            "delete" to "Delete",
            "share" to "Share",
            "copy" to "Copy",
            "paste" to "Paste",
            "select" to "Select",
            "save" to "Save",
            "edit" to "Edit",
            "close" to "Close",
            "back" to "Back",
            "next" to "Next",
            "previous" to "Previous",
            "done" to "Done",
            "continue" to "Continue"
        ),
        "ru" to mapOf(
            // Settings Screen
            "settings_title" to "Настройки",
            "settings_theme" to "Тема",
            "settings_language" to "Язык",
            "settings_data_management" to "Управление данными",
            "settings_about" to "О приложении",
            "theme_light" to "Светлая",
            "theme_dark" to "Тёмная",
            "theme_system" to "Системная",
            "language_english" to "English",
            "language_russian" to "Русский",
            "clear_all_data" to "Очистить все данные",
            "clear_all_data_description" to "Это удалит всю историю чатов и настройки",
            "app_name_full" to "SemeApp - AI Чат",
            "app_version" to "Версия 1.0.0",
            "powered_by" to "Работает на Google Gemini AI",
            
            // Navigation
            "nav_chat" to "Чат",
            "nav_history" to "История",
            "nav_prompts" to "Промпты",
            "nav_settings" to "Настройки",
            
            // Chat Screen
            "chat_placeholder" to "Введите сообщение...",
            "chat_send" to "Отправить",
            "chat_attach_image" to "Прикрепить изображение",
            "chat_take_photo" to "Сделать фото",
            "chat_typing" to "AI печатает...",
            "chat_error" to "Ошибка отправки сообщения",
            "chat_retry" to "Повторить",
            "chat_new_session" to "Новый чат",
            
            // History Screen
            "history_title" to "История чатов",
            "history_empty" to "История чатов пуста",
            "history_filter_all" to "Все",
            "history_filter_text" to "Только текст",
            "history_filter_image" to "С изображениями",
            "history_delete" to "Удалить",
            "history_share" to "Поделиться",
            
            // Prompt Screen
            "prompt_title" to "Промпты",
            "prompt_placeholder" to "Введите промпт...",
            "prompt_send" to "Отправить промпт",
            "prompt_clear" to "Очистить",
            "prompt_examples" to "Примеры промптов",
            "prompt_example_1" to "Напиши историю о...",
            "prompt_example_2" to "Объясни как...",
            "prompt_example_3" to "Создай стихотворение о...",
            
            // Common
            "error" to "Ошибка",
            "success" to "Успешно",
            "loading" to "Загрузка...",
            "cancel" to "Отмена",
            "ok" to "ОК",
            "yes" to "Да",
            "no" to "Нет",
            "delete" to "Удалить",
            "share" to "Поделиться",
            "copy" to "Копировать",
            "paste" to "Вставить",
            "select" to "Выбрать",
            "save" to "Сохранить",
            "edit" to "Редактировать",
            "close" to "Закрыть",
            "back" to "Назад",
            "next" to "Далее",
            "previous" to "Предыдущий",
            "done" to "Готово",
            "continue" to "Продолжить"
        )
    )
    
    fun get(key: String, language: String = "en"): String {
        return strings[language]?.get(key) ?: strings["en"]?.get(key) ?: key
    }
} 