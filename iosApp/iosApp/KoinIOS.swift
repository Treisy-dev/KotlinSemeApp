import shared

class KoinIOS {
    static let shared = KoinIOS()
    
    private init() {
        try! ExportedFunctionsIOS().initializeKoin()
    }
} 