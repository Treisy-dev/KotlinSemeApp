//
//  ContentView.swift
//  iosApp
//
//  Created by Кирилл Щёлоков on 21.06.2025.
//

import SwiftUI
import shared

struct ContentView: View {
    @State private var selectedTab = 0
    
    var body: some View {
        TabView(selection: $selectedTab) {
            // Chat Tab
            ChatView()
                .tabItem {
                    Image(systemName: "message.fill")
                    Text("Chat")
                }
                .tag(0)
            
            // History Tab
            HistoryView()
                .tabItem {
                    Image(systemName: "clock.fill")
                    Text("History")
                }
                .tag(1)
            
            // New Chat Tab
            PromptView()
                .tabItem {
                    Image(systemName: "plus.circle.fill")
                    Text("New Chat")
                }
                .tag(2)
            
            // Settings Tab
            SettingsView()
                .tabItem {
                    Image(systemName: "gear")
                    Text("Settings")
                }
                .tag(3)
        }
        .accentColor(.blue)
        .onChange(of: selectedTab) { newValue in
            trackTabSwitch(newValue)
        }
    }
    
    private func trackTabSwitch(_ tabIndex: Int) {
        let tabNames = ["chat", "history", "new_chat", "settings"]
        if tabIndex < tabNames.count {
            FirebaseAnalyticsManager.shared.trackTabSwitch(tabName: tabNames[tabIndex])
        }
    }
}

#Preview {
    ContentView()
}

import SwiftUI
import Lottie

struct LottieView: UIViewRepresentable {
    var filename: String


    func makeUIView(context: Context) -> LottieAnimationView {
        let animationView = LottieAnimationView(name: filename)
        animationView.contentMode = .scaleAspectFit
        animationView.loopMode = .loop
        return animationView
    }

    func updateUIView(_ uiView: LottieAnimationView, context: Context) {
        uiView.play()
    }
}
