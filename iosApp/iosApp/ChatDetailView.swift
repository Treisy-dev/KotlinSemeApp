import SwiftUI
import shared

struct ChatDetailView: View {
    let session: ChatSession
    @StateObject private var viewModel = ChatDetailViewModelWrapper()
    @StateObject private var localizationManager = LocalizationManager.shared
    @Environment(\.dismiss) private var dismiss
    @Environment(\.colorScheme) private var colorScheme
    @State private var isSharePresented = false
    @State private var shareItems: [Any] = []
    @State var shareTapped = false
    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                // Messages List
                if viewModel.isLoading {
                    Spacer()
                    ProgressView()
                        .scaleEffect(1.2)
                    Spacer()
                } else if viewModel.messages.isEmpty {
                    Spacer()
                    VStack(spacing: 16) {
                        Image(systemName: "message")
                            .font(.system(size: 64))
                            .foregroundColor(.secondary)
                        
                        Text(localizationManager.getString("history_empty"))
                            .font(.title2)
                            .fontWeight(.medium)
                        
                        Text(localizationManager.getString("history_no_messages"))
                            .font(.body)
                            .foregroundColor(.secondary)
                            .multilineTextAlignment(.center)
                    }
                    Spacer()
                } else {
                    ScrollViewReader { proxy in
                        ScrollView {
                            LazyVStack(spacing: 16) {
                                ForEach(viewModel.messages, id: \.id) { message in
                                    DetailMessageBubble(message: message)
                                }
                            }
                            .padding()
                        }
                        .onAppear {
                            // Scroll to bottom when messages load
                            if let lastMessage = viewModel.messages.last {
                                withAnimation(.easeInOut(duration: 0.5)) {
                                    proxy.scrollTo(lastMessage.id, anchor: .bottom)
                                }
                            }
                        }
                    }
                }
            }
            .navigationTitle(session.title.isEmpty ? "Chat" : session.title)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(localizationManager.getString("back")) {
                        dismiss()
                    }
                }
            }
        }

        .onAppear {
            viewModel.loadMessages(sessionId: session.id)
            FirebaseAnalyticsManager.shared.trackScreenView(screenName: "chat_detail")
        }

    }
}

struct DetailMessageBubble: View {
    let message: ChatMessage
    @Environment(\.colorScheme) private var colorScheme
    @StateObject private var localizationManager = LocalizationManager.shared
    @State var isSharePresented: Bool = false
    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            if message.isUser {
                Spacer(minLength: 60)
                
                VStack(alignment: .trailing, spacing: 8) {
                    // User message content
                    VStack(alignment: .trailing, spacing: 8) {
                        // Image if present
                        if let imagePath = message.imagePath, !imagePath.isEmpty {
                            AsyncImage(url: URL(fileURLWithPath: imagePath)) { image in
                                image
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(maxHeight: 200)
                                    .cornerRadius(12)
                            } placeholder: {
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(Color(.systemGray5))
                                    .frame(height: 200)
                                    .overlay(
                                        ProgressView()
                                            .scaleEffect(0.8)
                                    )
                            }
                        }
                        
                        // Text content
                        Text(message.content)
                            .padding(.horizontal, 16)
                            .padding(.vertical, 12)
                            .background(
                                LinearGradient(
                                    colors: [Color.blue, Color.blue.opacity(0.8)],
                                    startPoint: .topLeading,
                                    endPoint: .bottomTrailing
                                )
                            )
                            .foregroundColor(.white)
                            .cornerRadius(18)
                            .cornerRadius(4, corners: [.topLeft])
                    }
                    
                    // Timestamp
                    Text(formatTimestamp(message.timestamp))
                        .font(.caption2)
                        .foregroundColor(.secondary)
                        .padding(.trailing, 4)
                }
            } else {
                VStack(alignment: .leading, spacing: 8) {
                    // AI message content
                    HStack(alignment: .top, spacing: 8) {
                        // AI Avatar
                        Circle()
                            .fill(
                                LinearGradient(
                                    colors: [Color.purple, Color.blue],
                                    startPoint: .topLeading,
                                    endPoint: .bottomTrailing
                                )
                            )
                            .frame(width: 32, height: 32)
                            .overlay(
                                Image(systemName: "sparkles")
                                    .font(.system(size: 16, weight: .medium))
                                    .foregroundColor(.white)
                            )
                        
                        // Message bubble
                        VStack(alignment: .leading, spacing: 8) {
                            Text(message.content)
                                .padding(.horizontal, 16)
                                .padding(.vertical, 12)
                                .background(
                                    colorScheme == .dark ? Color(.systemGray6) : Color(.systemGray5)
                                )
                                .foregroundColor(.primary)
                                .cornerRadius(18)
                                .cornerRadius(4, corners: [.topLeft])
                            
                            // Share button for AI responses
                            HStack {
                                Button(action: {
                                    isSharePresented = true
  
                                }) {
                                    HStack(spacing: 4) {
                                        Image(systemName: "square.and.arrow.up")
                                            .font(.system(size: 12, weight: .medium))
                                        Text(localizationManager.getString("share"))
                                            .font(.caption)
                                            .fontWeight(.medium)
                                    }
                                    .foregroundColor(.blue)
                                    .padding(.horizontal, 12)
                                    .padding(.vertical, 6)
                                    .background(
                                        RoundedRectangle(cornerRadius: 12)
                                            .fill(Color.blue.opacity(0.1))
                                    )
                                }
                                
                                Spacer()
                            }
                            .padding(.leading, 16)
                        }
                    }
                    
                    // Timestamp
                    Text(formatTimestamp(message.timestamp))
                        .font(.caption2)
                        .foregroundColor(.secondary)
                        .padding(.leading, 40)
                }
                
                Spacer(minLength: 60)
            }
        }
        .animation(.easeInOut(duration: 0.3), value: message.id)
        .sheet(isPresented: $isSharePresented) {
            ShareSheet(items: [message.content])
            .ignoresSafeArea()
        }
    }

}

// Extension for rounded corners
extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape(RoundedCorner(radius: radius, corners: corners))
    }
}

struct RoundedCorner: Shape {
    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(
            roundedRect: rect,
            byRoundingCorners: corners,
            cornerRadii: CGSize(width: radius, height: radius)
        )
        return Path(path.cgPath)
    }
}

import SwiftUI


struct ShareSheet: UIViewControllerRepresentable {
    let items: [Any]
    func makeUIViewController(context: Context) -> UIActivityViewController {
        let controller = UIActivityViewController(activityItems: items, applicationActivities: nil)
        return controller
    }
    func updateUIViewController(_ uiViewController: UIActivityViewController, context: Context) {}
}

