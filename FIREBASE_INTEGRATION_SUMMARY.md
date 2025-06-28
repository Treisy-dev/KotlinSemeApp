# Firebase Integration Summary

## ✅ Что было настроено

### 1. Firebase Analytics
- **Отслеживание экранов**: При открытии каждого экрана отправляется событие в Firebase Analytics
  - `launch_chat` - экран чата
  - `launch_history` - экран истории  
  - `launch_new_chat` - экран нового чата
  - `launch_settings` - экран настроек
- **Отслеживание событий**:
  - `app_launch` - запуск приложения
  - `tab_switch` - переключение табов
  - `message_sent` - отправка сообщения
  - `settings_changed` - изменение настроек
  - `data_cleared` - очистка данных
  - `data_exported` - экспорт данных

### 2. Firebase Crashlytics
- **Автоматическое логирование ошибок** из всех ViewModels
- **Глобальный обработчик исключений**
- **Логирование состояния приложения**
- **Пользовательские ключи для отладки**

## 📁 Созданные файлы

### Новые файлы:
- `iosApp/iosApp/FirebaseAnalyticsManager.swift` - менеджер для Firebase Analytics
- `iosApp/iosApp/ErrorHandler.swift` - обработчик ошибок для Crashlytics
- `iosApp/iosApp/GoogleService-Info.plist` - конфигурация Firebase
- `iosApp/Configuration/Firebase.xcconfig` - настройки Firebase
- `iosApp/FIREBASE_SETUP.md` - документация по настройке

### Обновленные файлы:
- `iosApp/Podfile` - добавлены Firebase зависимости
- `iosApp/iosApp/iOSApp.swift` - инициализация Firebase
- `iosApp/iosApp/ContentView.swift` - отслеживание переключения табов
- `iosApp/iosApp/ChatView.swift` - отслеживание экрана и событий
- `iosApp/iosApp/HistoryView.swift` - отслеживание экрана
- `iosApp/iosApp/PromptView.swift` - отслеживание экрана и событий
- `iosApp/iosApp/SettingsView.swift` - отслеживание экрана и событий
- `iosApp/iosApp/ViewModels.swift` - обработка ошибок

## 🔧 Установленные зависимости

```ruby
pod 'FirebaseAnalytics'
pod 'FirebaseCrashlytics'
```

## 📊 Отслеживаемые события

### Экраны:
- `launch_home` - главный экран
- `launch_chat` - экран чата
- `launch_history` - экран истории
- `launch_new_chat` - экран нового чата
- `launch_settings` - экран настроек
- `launch_search` - экран поиска (готов к использованию)
- `launch_profile` - экран профиля (готов к использованию)

### Пользовательские события:
- `app_launch` - запуск приложения
- `tab_switch` - переключение табов
- `message_sent` - отправка сообщения
- `settings_changed` - изменение настроек
- `data_cleared` - очистка данных
- `data_exported` - экспорт данных
- `privacy_policy_opened` - открытие политики конфиденциальности
- `terms_of_service_opened` - открытие условий использования

## 🚨 Обработка ошибок

Все ошибки автоматически логируются в Firebase Crashlytics с контекстом:
- Источник ошибки (ViewModel)
- Дополнительная информация (количество сообщений, состояние загрузки и т.д.)
- Временная метка
- Стек вызовов

## 🎯 Готово к использованию

Firebase полностью интегрирован и готов к использованию. Все события будут отправляться в Firebase Console, а ошибки - в Crashlytics.

Для просмотра данных:
1. Откройте [Firebase Console](https://console.firebase.google.com/)
2. Выберите проект `kotlinsemeapp`
3. Перейдите в раздел Analytics для просмотра событий
4. Перейдите в раздел Crashlytics для просмотра ошибок 