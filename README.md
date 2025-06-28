# Запуск desktop:
В корневом каталоге: ./gradlew composeApp:Run

# Запуск ios:
В корневом каталоге:
./gradlew :shared:podPublishDebugXCFramework  
./gradlew :shared:generateDummyFramework  
cd iosApp  
pod install  

# Запуск Android:
просто в Android Studio
