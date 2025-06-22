import Foundation
import shared

// Helper function to format Kotlin Instant to String
func formatTimestamp(_ timestamp: Kotlinx_datetimeInstant) -> String {
    // Convert Kotlin Instant to seconds since epoch
    let seconds = timestamp.epochSeconds
    let date = Date(timeIntervalSince1970: TimeInterval(seconds))
    
    let formatter = DateFormatter()
    let now = Date()
    let calendar = Calendar.current
    
    if calendar.isDateInToday(date) {
        formatter.dateFormat = "HH:mm"
    } else if calendar.isDate(date, equalTo: now, toGranularity: .year) {
        formatter.dateFormat = "MMM d"
    } else {
        formatter.dateFormat = "yyyy"
    }
    
    return formatter.string(from: date)
} 