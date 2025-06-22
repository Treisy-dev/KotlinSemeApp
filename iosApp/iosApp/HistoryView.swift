import SwiftUI
import shared

struct HistoryView: View {
    @StateObject private var viewModel = HistoryViewModelWrapper()
    @State private var selectedFilter: FilterType = .all
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        NavigationView {
            VStack {
                // Filter Chips
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 12) {
                        FilterChip(
                            title: "All",
                            isSelected: selectedFilter == .all,
                            action: { selectedFilter = .all; viewModel.setFilter(.all) }
                        )
                        
                        FilterChip(
                            title: "Text Only",
                            isSelected: selectedFilter == .textOnly,
                            action: { selectedFilter = .textOnly; viewModel.setFilter(.textOnly) }
                        )
                        
                        FilterChip(
                            title: "With Image",
                            isSelected: selectedFilter == .withImage,
                            action: { selectedFilter = .withImage; viewModel.setFilter(.withImage) }
                        )
                    }
                    .padding(.horizontal)
                }
                .padding(.vertical, 8)
                
                // Sessions List
                if viewModel.isLoading {
                    Spacer()
                    ProgressView()
                    Spacer()
                } else if viewModel.sessions.isEmpty {
                    Spacer()
                    VStack(spacing: 16) {
                        Image(systemName: "clock")
                            .font(.system(size: 64))
                            .foregroundColor(.secondary)
                        
                        Text("No chat history")
                            .font(.title2)
                            .fontWeight(.medium)
                        
                        Text("Start a new conversation to see it here")
                            .font(.body)
                            .foregroundColor(.secondary)
                            .multilineTextAlignment(.center)
                    }
                    Spacer()
                } else {
                    List {
                        ForEach(viewModel.sessions, id: \.id) { session in
                            SessionRow(session: session) {
                                viewModel.openSession(session.id)
                            } onDelete: {
                                viewModel.deleteSession(session.id)
                            }
                        }
                    }
                    .listStyle(PlainListStyle())
                }
            }
            .navigationTitle("Chat History")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Done") {
                        dismiss()
                    }
                }
                
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { viewModel.clearAllHistory() }) {
                        Image(systemName: "trash")
                    }
                }
            }
        }
        .onAppear {
            viewModel.loadSessions()
        }
    }
}

struct FilterChip: View {
    let title: String
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.body)
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .background(isSelected ? Color.blue : Color(.systemGray5))
                .foregroundColor(isSelected ? .white : .primary)
                .cornerRadius(20)
        }
    }
}

struct SessionRow: View {
    let session: ChatSession
    let onTap: () -> Void
    let onDelete: () -> Void
    
    var body: some View {
        HStack {
            // Icon
            Image(systemName: session.hasImage ? "photo" : "message")
                .font(.title2)
                .foregroundColor(.blue)
                .frame(width: 24, height: 24)
            
            // Content
            VStack(alignment: .leading, spacing: 4) {
                Text(session.title)
                    .font(.body)
                    .fontWeight(.medium)
                    .lineLimit(1)
                
                Text(session.lastMessage)
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .lineLimit(2)
                
                Text(formatTimestamp(session.timestamp))
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
            
            Spacer()
            
            // Delete button
            Button(action: onDelete) {
                Image(systemName: "trash")
                    .foregroundColor(.red)
            }
        }
        .padding(.vertical, 8)
        .contentShape(Rectangle())
        .onTapGesture {
            onTap()
        }
    }
}

#Preview {
    HistoryView()
} 