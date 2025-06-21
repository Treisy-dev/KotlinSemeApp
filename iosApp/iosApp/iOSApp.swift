import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        KoinIOSKt.doInitKoin_iOS()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
