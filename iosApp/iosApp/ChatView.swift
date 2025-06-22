import SwiftUI
import shared

struct ChatView: View {
    @StateObject private var viewModel = ChatViewModelWrapper()
    @State private var messageText = ""
    @State private var showingHistory = false
    @State private var showingSettings = false
    
    var body: some View {
        NavigationView {
            VStack {
                // Messages List
                ScrollViewReader { proxy in
                    ScrollView {
                        LazyVStack(spacing: 12) {
                            if viewModel.messages.isEmpty {
                                // Empty state
                                VStack(spacing: 16) {
                                    Image(systemName: "message")
                                        .font(.system(size: 64))
                                        .foregroundColor(.secondary)
                                    
                                    Text("Start a conversation")
                                        .font(.title2)
                                        .fontWeight(.medium)
                                    
                                    Text("Ask Gemini anything or share an image for analysis")
                                        .font(.body)
                                        .foregroundColor(.secondary)
                                        .multilineTextAlignment(.center)
                                }
                                .frame(maxWidth: .infinity, maxHeight: .infinity)
                                .padding()
                            } else {
                                // Messages
                                ForEach(viewModel.messages, id: \.id) { message in
                                    MessageBubble(message: message)
                                }
                                
                                // Loading indicator
                                if viewModel.isLoading {
                                    HStack {
                                        ProgressView()
                                            .scaleEffect(0.8)
                                        Text("Gemini is thinking...")
                                            .font(.body)
                                            .foregroundColor(.secondary)
                                        Spacer()
                                    }
                                    .padding(.horizontal)
                                    .padding(.vertical, 8)
                                    .background(Color(.systemGray6))
                                    .cornerRadius(12)
                                    .padding(.horizontal)
                                }
                            }
                        }
                        .padding()
                    }
                    .onChange(of: viewModel.messages.count) { _ in
                        if !viewModel.messages.isEmpty {
                            withAnimation {
                                proxy.scrollTo(viewModel.messages.last?.id, anchor: .bottom)
                            }
                        }
                    }
                }
                
                // Input Area
                VStack(spacing: 0) {
                    Divider()
                    HStack(spacing: 12) {
                        TextField("Type your message...", text: $messageText, axis: .vertical)
                            .textFieldStyle(RoundedBorderTextFieldStyle())
                            .lineLimit(1...4)
                        
                        Button(action: sendMessage) {
                            Image(systemName: "arrow.up.circle.fill")
                                .font(.title2)
                                .foregroundColor(messageText.isEmpty || viewModel.isLoading ? .secondary : .blue)
                        }
                        .disabled(messageText.isEmpty || viewModel.isLoading)
                    }
                    .padding()
                }
                .background(Color(.systemBackground))
            }
            .navigationTitle("Chat with Gemini")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: { showingHistory = true }) {
                        Image(systemName: "clock")
                    }
                }
                
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { showingSettings = true }) {
                        Image(systemName: "gear")
                    }
                }
            }
            .sheet(isPresented: $showingHistory) {
                HistoryView()
            }
            .sheet(isPresented: $showingSettings) {
                SettingsView()
            }
        }
        .onAppear {
            viewModel.loadMessages()
            FirebaseAnalyticsManager.shared.trackScreenView(screenName: FirebaseAnalyticsManager.ScreenNames.chat)
        }
    }
    
    private func sendMessage() {
        guard !messageText.isEmpty else { return }
        viewModel.sendMessage(messageText)
        FirebaseAnalyticsManager.shared.trackMessageSent()
        messageText = ""
    }
}

struct MessageBubble: View {
    let message: ChatMessage
    
    var body: some View {
        HStack {
            if message.isUser {
                Spacer()
                VStack(alignment: .trailing, spacing: 4) {
                    Text(message.content)
                        .padding(.horizontal, 16)
                        .padding(.vertical, 12)
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(18)
                    
                    Text(formatTimestamp(message.timestamp))
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
            } else {
                VStack(alignment: .leading, spacing: 4) {
                    Text(message.content)
                        .padding(.horizontal, 16)
                        .padding(.vertical, 12)
                        .background(Color(.systemGray5))
                        .foregroundColor(.primary)
                        .cornerRadius(18)
                    
                    Text(formatTimestamp(message.timestamp))
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
                Spacer()
            }
        }
    }
}

#Preview {
    ChatView()
} 