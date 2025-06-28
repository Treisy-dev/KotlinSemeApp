package org.example.project.data.firebase

import androidx.core.os.bundleOf
import org.example.project.firebase.FirebaseAnalytics

typealias LibraryFirebaseAnalytics = com.google.firebase.analytics.FirebaseAnalytics

internal class AndroidFirebaseAnalytics(
    private val firebaseAnalytics: LibraryFirebaseAnalytics
) : FirebaseAnalytics {

    override fun logEvent(eventName: String, parameters: Map<String, String>) {
        val bundle = bundleOf().apply {
            parameters.forEach { (key, value) ->
                putString(key, value)
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }
}
