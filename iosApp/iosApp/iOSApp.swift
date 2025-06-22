import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        // Initialize Koin for iOS
        _ = KoinIOS.shared
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
