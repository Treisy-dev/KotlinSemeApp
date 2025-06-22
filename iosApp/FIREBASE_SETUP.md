# Firebase Setup для iOS приложения

## Что было настроено

### 1. Firebase Analytics
- ✅ Отслеживание запуска приложения (`app_launch`)
- ✅ Отслеживание переключения табов (`tab_switch`)
- ✅ Отслеживание экранов:
  - `launch_chat` - экран чата
  - `launch_history` - экран истории
  - `launch_new_chat` - экран нового чата
  - `launch_settings` - экран настроек
- ✅ Отслеживание событий:
  - `message_sent` - отправка сообщения
  - `settings_changed` - изменение настроек
  - `data_cleared` - очистка данных
  - `data_exported` - экспорт данных
  - `privacy_policy_opened` - открытие политики конфиденциальности
  - `terms_of_service_opened` - открытие условий использования

### 2. Firebase Crashlytics
- ✅ Автоматическое логирование ошибок
- ✅ Логирование пользовательских ошибок
- ✅ Отслеживание состояния приложения
- ✅ Глобальный обработчик исключений

## Файлы конфигурации

### GoogleService-Info.plist
Файл `GoogleService-Info.plist` должен быть добавлен в iOS проект. Убедитесь, что он включен в target приложения.

### Podfile
Добавлены зависимости:
```ruby
pod 'FirebaseAnalytics'
pod 'FirebaseCrashlytics'
```

## Использование

### Отслеживание экранов
```swift
FirebaseAnalyticsManager.shared.trackScreenView(screenName: "launch_home")
```

### Отслеживание событий
```swift
FirebaseAnalyticsManager.shared.trackCustomEvent(name: "custom_event", parameters: ["key": "value"])
```

### Логирование ошибок
```swift
ErrorHandler.shared.logError(error, context: "context")
ErrorHandler.shared.logCustomError(message: "Error message", context: "context")
```

## Константы экранов

Доступные константы для отслеживания экранов:
- `FirebaseAnalyticsManager.ScreenNames.home`
- `FirebaseAnalyticsManager.ScreenNames.chat`
- `FirebaseAnalyticsManager.ScreenNames.history`
- `FirebaseAnalyticsManager.ScreenNames.newChat`
- `FirebaseAnalyticsManager.ScreenNames.settings`
- `FirebaseAnalyticsManager.ScreenNames.search`
- `FirebaseAnalyticsManager.ScreenNames.profile`

## Настройка в Firebase Console

1. Создайте проект в [Firebase Console](https://console.firebase.google.com/)
2. Добавьте iOS приложение
3. Скачайте `GoogleService-Info.plist`
4. Включите Analytics и Crashlytics в консоли

## Отладка

Для отладки в консоли будут выводиться сообщения:
- 📊 Firebase Analytics события
- 🚨 Crashlytics ошибки

## Примечания

- В debug режиме Crashlytics работает с задержкой
- Analytics события отправляются в реальном времени
- Все ошибки автоматически логируются в Crashlytics 