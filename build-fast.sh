#!/bin/bash

# Скрипт для быстрой сборки Kotlin Multiplatform проекта
# Использование: ./build-fast.sh [platform]
# platform: android, desktop, ios, all

set -e

PLATFORM=${1:-desktop}

echo "🚀 Быстрая сборка для платформы: $PLATFORM"

case $PLATFORM in
    "android")
        echo "📱 Сборка Android..."
        ./gradlew :composeApp:assembleDebug --parallel --configure-on-demand
        ;;
    "desktop")
        echo "🖥️  Сборка Desktop..."
        ./gradlew :composeApp:run --parallel --configure-on-demand
        ;;
    "ios")
        echo "🍎 Сборка iOS..."
        ./gradlew :shared:podInstall --parallel --configure-on-demand
        cd iosApp && pod install && cd ..
        echo "Откройте iosApp.xcworkspace в Xcode"
        ;;
    "all")
        echo "🌍 Сборка всех платформ..."
        ./gradlew build --parallel --configure-on-demand
        ;;
    *)
        echo "❌ Неизвестная платформа: $PLATFORM"
        echo "Доступные платформы: android, desktop, ios, all"
        exit 1
        ;;
esac

echo "✅ Сборка завершена!" 