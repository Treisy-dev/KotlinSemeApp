import Foundation
import FirebaseAnalytics

class FirebaseAnalyticsManager {
    static let shared = FirebaseAnalyticsManager()
    
    private init() {}
    
    // MARK: - Screen Tracking
    func trackScreenView(screenName: String, screenClass: String? = nil) {
        Analytics.logEvent(AnalyticsEventScreenView, parameters: [
            AnalyticsParameterScreenName: screenName,
            AnalyticsParameterScreenClass: screenClass ?? screenName
        ])
        print("📊 Firebase Analytics: Screen view tracked - \(screenName)")
    }
    
    // MARK: - Custom Events
    func trackCustomEvent(name: String, parameters: [String: Any]? = nil) {
        Analytics.logEvent(name, parameters: parameters)
        print("📊 Firebase Analytics: Custom event tracked - \(name)")
    }
    
    // MARK: - App Events
    func trackAppLaunch() {
        trackCustomEvent(name: "app_launch")
    }
    
    func trackTabSwitch(tabName: String) {
        trackCustomEvent(name: "tab_switch", parameters: ["tab_name": tabName])
    }
    
    // MARK: - Chat Events
    func trackMessageSent() {
        trackCustomEvent(name: "message_sent")
    }
    
    func trackMessageReceived() {
        trackCustomEvent(name: "message_received")
    }
    
    // MARK: - Settings Events
    func trackSettingsChanged(settingName: String, newValue: String) {
        trackCustomEvent(name: "settings_changed", parameters: [
            "setting_name": settingName,
            "new_value": newValue
        ])
    }
    
    // MARK: - Error Events
    func trackError(error: Error, context: String) {
        trackCustomEvent(name: "app_error", parameters: [
            "error_description": error.localizedDescription,
            "context": context
        ])
    }
}

// MARK: - Screen Names Constants
extension FirebaseAnalyticsManager {
    enum ScreenNames {
        static let home = "launch_home"
        static let chat = "launch_chat"
        static let history = "launch_history"
        static let newChat = "launch_new_chat"
        static let settings = "launch_settings"
        static let search = "launch_search"
        static let profile = "launch_profile"
    }
} 