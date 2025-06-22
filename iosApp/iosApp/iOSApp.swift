import SwiftUI
import shared
import FirebaseCore
import FirebaseAnalytics
import FirebaseCrashlytics

@main
struct iOSApp: App {
    init() {
        // Initialize Firebase
        FirebaseApp.configure()
        
        // Setup global error handling
        ErrorHandler.setupGlobalErrorHandling()
        
        // Initialize Koin for iOS
        _ = KoinIOS.shared
        
        // Track app launch
        FirebaseAnalyticsManager.shared.trackAppLaunch()
        
        // Log app state
        ErrorHandler.shared.logAppState("app_launched")
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
