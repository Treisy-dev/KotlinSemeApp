import shared

class KoinIOS {
    static let shared = KoinIOS()
    
    private init() {
        // Initialize Koin for iOS
        KoinIOSKt.doInitKoin_iOS()
    }
} 