import Foundation
import FirebaseCrashlytics

class ErrorHandler {
    static let shared = ErrorHandler()
    
    private init() {}
    
    // MARK: - Error Logging
    func logError(_ error: Error, context: String? = nil) {
        let crashlytics = Crashlytics.crashlytics()
        
        // Log the error to Crashlytics
        crashlytics.record(error: error)
        
        // Add custom keys for better debugging
        if let context = context {
            crashlytics.setCustomValue(context, forKey: "error_context")
        }
        
        crashlytics.setCustomValue(Date(), forKey: "error_timestamp")
        
        // Also log to Firebase Analytics
        FirebaseAnalyticsManager.shared.trackError(error: error, context: context ?? "unknown")
        
        print("🚨 Error logged to Crashlytics: \(error.localizedDescription)")
    }
    
    // MARK: - Custom Error Logging
    func logCustomError(message: String, context: String? = nil, additionalInfo: [String: Any]? = nil) {
        let crashlytics = Crashlytics.crashlytics()
        
        // Create a custom error
        let customError = NSError(domain: "com.Kirill.iosApp", code: -1, userInfo: [
            NSLocalizedDescriptionKey: message
        ])
        
        crashlytics.record(error: customError)
        
        // Add custom keys
        if let context = context {
            crashlytics.setCustomValue(context, forKey: "error_context")
        }
        
        if let additionalInfo = additionalInfo {
            for (key, value) in additionalInfo {
                crashlytics.setCustomValue(value, forKey: key)
            }
        }
        
        crashlytics.setCustomValue(Date(), forKey: "error_timestamp")
        
        print("🚨 Custom error logged to Crashlytics: \(message)")
    }
    
    // MARK: - User Information
    func setUserIdentifier(_ userID: String) {
        Crashlytics.crashlytics().setUserID(userID)
    }
    
    func setUserProperty(key: String, value: String) {
        Crashlytics.crashlytics().setCustomValue(value, forKey: key)
    }
    
    // MARK: - App State Logging
    func logAppState(_ state: String) {
        Crashlytics.crashlytics().setCustomValue(state, forKey: "app_state")
        Crashlytics.crashlytics().setCustomValue(Date(), forKey: "state_timestamp")
    }
}

// MARK: - Global Error Handler
extension ErrorHandler {
    static func setupGlobalErrorHandling() {
        // Set up uncaught exception handler
        NSSetUncaughtExceptionHandler { exception in
            ErrorHandler.shared.logCustomError(
                message: exception.reason ?? "Uncaught exception",
                context: "uncaught_exception",
                additionalInfo: [
                    "exception_name": exception.name.rawValue,
                    "call_stack": exception.callStackSymbols.joined(separator: "\n")
                ]
            )
        }
    }
} 