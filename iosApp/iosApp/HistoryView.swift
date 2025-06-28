import SwiftUI
import shared

struct HistoryView: View {
    @StateObject private var viewModel = HistoryViewModelWrapper()
    @StateObject private var localizationManager = LocalizationManager.shared
    @State private var selectedFilter: FilterType = .all
    @State private var selectedSession: ChatSession? = nil
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        NavigationView {
            VStack {
                // Filter Chips
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 12) {
                        FilterChip(
                            title: localizationManager.getString("history_filter_all"),
                            isSelected: selectedFilter == .all,
                            action: { selectedFilter = .all; viewModel.setFilter(.all) }
                        )
                        
                        FilterChip(
                            title: localizationManager.getString("history_filter_text"),
                            isSelected: selectedFilter == .textOnly,
                            action: { selectedFilter = .textOnly; viewModel.setFilter(.textOnly) }
                        )
                        
                        FilterChip(
                            title: localizationManager.getString("history_filter_image"),
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
                        
                        Text(localizationManager.getString("history_empty"))
                            .font(.title2)
                            .fontWeight(.medium)
                        
                        Text(localizationManager.getString("history_subtitle"))
                            .font(.body)
                            .foregroundColor(.secondary)
                            .multilineTextAlignment(.center)
                    }
                    Spacer()
                } else {
                    List {
                        ForEach(viewModel.sessions, id: \.id) { session in
                            HStack {
                                
                                SessionRow(session: session) {
                                    selectedSession = session
                                } onDelete: {
                                    viewModel.deleteSession(session.id)
                                }
                                .swipeActions {
                                    Button{
                                        viewModel.deleteSession(session.id)
                                    } label: {
                                      Image(systemName: "trash")
                                        .font(.headline)
                                        .foregroundColor(.red)
                                        .frame(width: 40, height: 40)
                                    }
                                }
                                Spacer()

                            }

                        }
                    }
                    .listStyle(PlainListStyle())
                }
            }
            .navigationTitle(localizationManager.getString("history_title"))
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { viewModel.clearAllHistory() }) {
                        Image(systemName: "trash")
                    }
                }
            }
        }
        .fullScreenCover(item: $selectedSession) { session in
            ChatDetailView(session: session)
        }
        .onAppear {
            viewModel.loadSessions()
            FirebaseAnalyticsManager.shared.trackScreenView(screenName: FirebaseAnalyticsManager.ScreenNames.history)
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
      // Первая кнопка: без Spacer внутри
      Button(action: onTap) {
        HStack {
          Image(systemName: session.hasImage ? "photo" : "message")
            .font(.title2)
            .foregroundColor(.blue)
            .frame(width: 24, height: 24)

          VStack(alignment: .leading, spacing: 4) {
            Text(session.title).lineLimit(1).fontWeight(.medium)
            Text(session.lastMessage).lineLimit(2).font(.caption).foregroundColor(.secondary)
            Text(formatTimestamp(session.timestamp))
              .font(.caption2)
              .foregroundColor(.secondary)
          }
        }
      }
      // Теперь Spacer — между кнопками, а не внутри лейбла
      Spacer()
      // Вторая кнопка остаётся на своём месте

    }
    .padding(.vertical, 8)
  }
}

// Extension to make ChatSession identifiable for sheet presentation
extension ChatSession: Identifiable {}

#Preview {
    HistoryView()
} 
