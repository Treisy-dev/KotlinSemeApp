import SwiftUI
import Shared

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