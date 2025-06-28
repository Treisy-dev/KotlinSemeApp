# SemeApp - AI Chat Application

Многоплатформенное приложение для чата с AI (Gemini) на Kotlin Multiplatform с поддержкой Android, iOS и Desktop.

## 🚀 Новый дизайн с Tab Bar

Приложение теперь использует современный дизайн с таб баром для навигации между основными экранами:

### 📱 iOS (SwiftUI)
- **Chat** - основной чат с Gemini
- **History** - история чатов
- **New Chat** - создание нового чата
- **Settings** - настройки приложения

### 🤖 Android (Compose)
- **Chat** - основной чат с Gemini
- **History** - история чатов  
- **New Chat** - создание нового чата
- **Settings** - настройки приложения

### 🖥️ Desktop (Compose)
- **Chat** - основной чат с Gemini
- **History** - история чатов
- **New Chat** - создание нового чата
- **Settings** - настройки приложения

## 🎨 Дизайн

### Иконки
- **iOS**: Используются системные SF Symbols (`message.fill`, `clock.fill`, `plus.circle.fill`, `gear`)
- **Android**: Используются Material Icons (`Message`, `History`, `Add`, `Settings`)
- **Desktop**: Используются Material Icons

### Цветовая схема
- Поддержка светлой и темной темы
- Material 3 Design System
- Адаптивные цвета для каждой платформы

## 🛠️ Технологии

- **Kotlin Multiplatform** - общая бизнес-логика
- **SwiftUI** - iOS UI
- **Jetpack Compose** - Android UI
- **Compose Multiplatform** - Desktop UI
- **Koin** - Dependency Injection
- **Voyager** - Навигация (Desktop)
- **Navigation Compose** - Навигация (Android)
- **Firebase** - Аналитика и крэш-репорты

## 📁 Структура проекта

```
├── shared/                    # Общий код
│   ├── src/commonMain/       # Общая бизнес-логика
│   ├── src/androidMain/      # Android-специфичный код
│   ├── src/iosMain/          # iOS-специфичный код
│   └── src/desktopMain/      # Desktop-специфичный код
├── composeApp/               # Android & Desktop приложения
│   └── src/
│       ├── androidMain/      # Android UI
│       └── desktopMain/      # Desktop UI
└── iosApp/                   # iOS приложение
    └── iosApp/
        ├── ContentView.swift # Основной TabView
        ├── ChatView.swift    # Экран чата
        ├── HistoryView.swift # Экран истории
        ├── PromptView.swift  # Экран нового чата
        ├── SettingsView.swift # Экран настроек
        └── ViewModels.swift  # iOS ViewModels
```

## 🚀 Запуск

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
```bash
./gradlew :shared:podInstall
# Откройте iosApp/iosApp.xcworkspace в Xcode
```

### Desktop
```bash
./gradlew :composeApp:run
```

### Быстрая сборка
```bash
./build-fast.sh
```

## 📱 Функции

- 💬 Чат с AI (Gemini)
- 📸 Поддержка изображений
- 📚 История чатов
- ⚙️ Настройки темы и языка
- 🔄 Синхронизация между платформами
- 📊 Firebase аналитика

## 🎯 Особенности дизайна

1. **Единообразный UX** - одинаковый интерфейс на всех платформах
2. **Нативные иконки** - используются системные иконки каждой платформы
3. **Адаптивный дизайн** - оптимизирован для разных размеров экранов
4. **Material 3** - современный дизайн-система
5. **Темная тема** - поддержка системных настроек

## 🔧 Настройка

### iOS
- Добавлены разрешения для камеры и фото библиотеки
- Настроен TabView с системными иконками
- Интеграция с shared модулем через Koin

### Android  
- Настроен Navigation Compose с TabRow
- Material 3 дизайн
- Интеграция с shared модулем

### Desktop
- Compose Multiplatform UI
- Навигация через TabRow
- Адаптивный дизайн для больших экранов

## 📋 Requirements

- **Android**: API 26+ (Android 8.0+)
- **iOS**: iOS 16.0+
- **Desktop**: JVM 11+
- **Kotlin**: 2.1.21+
- **Gradle**: 8.0+

## 🛠️ Setup and Installation

### Prerequisites

1. Install Android Studio or IntelliJ IDEA
2. Install Xcode (for iOS development)
3. Install JDK 11 or higher
4. Clone the repository

### Project Structure

```
SemeApp/
├── composeApp/          # Android and Desktop applications
├── shared/             # Common Kotlin Multiplatform code
├── iosApp/             # iOS application
├── gradle/             # Gradle configuration
└── README.md
```

### Building the Project

#### Android
```bash
./gradlew :composeApp:assembleDebug
```

#### Desktop
```bash
./gradlew :composeApp:run
```

#### iOS
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select your target device or simulator
3. Build and run the project

### Running on Different Platforms

#### Android
1. Connect an Android device or start an emulator
2. Run `./gradlew :composeApp:installDebug`
3. The app will be installed and launched automatically

#### Desktop
1. Run `./gradlew :composeApp:run`
2. The desktop application will start with a native window

#### iOS
1. Open the project in Xcode
2. Select your target device or simulator
3. Press Cmd+R to build and run

## 🏗️ Architecture

### MVI Pattern
The application follows the Model-View-Intent (MVI) architecture:

- **State**: Immutable data classes representing UI state
- **Events**: User actions and system events
- **Effects**: One-time side effects (navigation, toasts, etc.)
- **ViewModel**: Business logic and state management

### Module Structure

#### Shared Module (`shared/`)
- **UI Components**: Common Compose UI components
- **ViewModels**: MVI-based ViewModels
- **Navigation**: Voyager navigation setup
- **DI**: Koin dependency injection modules
- **Resources**: Strings and images via Moko Resources
- **Firebase**: Analytics and Crashlytics integration

#### Platform Modules
- **composeApp**: Android and Desktop applications
- **iosApp**: iOS application with SwiftUI wrapper

### Key Technologies

- **Kotlin Multiplatform**: Cross-platform development
- **Compose Multiplatform**: UI framework
- **Koin**: Dependency injection
- **Voyager**: Navigation
- **Moko Resources**: Resource management
- **Firebase**: Analytics and crash reporting
- **Material Design 3**: Design system

## 🔧 Configuration

### Firebase Setup

#### Android
1. Add `google-services.json` to `composeApp/`
2. Firebase is automatically initialized in `SemeApplication`

#### iOS
1. Add `GoogleService-Info.plist` to `iosApp/iosApp/`
2. Configure Firebase in Xcode project settings

### Moko Resources Setup

#### iOS Configuration
Add the following build phase to your Xcode project:

```bash
cd "$SRCROOT/.."
./gradlew :shared:generateMRiosMain
```

This generates the necessary iOS resource files.

## 📱 Screenshots

*Screenshots will be added here*

## 🚀 Development

### Adding New Features

1. **New Screen**: Create ViewModel with MVI pattern in `shared/src/commonMain/kotlin/org/example/project/screens/`
2. **Navigation**: Add screen to `SharedScreen` sealed class
3. **DI**: Register ViewModel in Koin module
4. **UI**: Implement Compose UI in the screen class

### Code Style

- Follow Kotlin coding conventions
- Use MVI pattern for all screens
- Implement proper error handling
- Add unit tests for ViewModels
- Use meaningful commit messages

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Troubleshooting

### Common Issues

#### Build Failures
- Ensure all dependencies are properly synced
- Clean and rebuild the project: `./gradlew clean build`
- Check Kotlin and Gradle versions compatibility

#### iOS Build Issues
- Verify Xcode version compatibility
- Check iOS deployment target settings
- Ensure all iOS-specific dependencies are properly configured

#### Desktop Issues
- Verify JDK version (11+ required)
- Check system-specific dependencies
- Ensure proper window management setup

### Getting Help

If you encounter issues:
1. Check the troubleshooting section above
2. Search existing issues
3. Create a new issue with detailed information

## 📞 Support

For support and questions:
- Create an issue on GitHub
- Check the documentation
- Review the code examples

---

**Note**: This is a development version. Some features may be in progress or require additional configuration.