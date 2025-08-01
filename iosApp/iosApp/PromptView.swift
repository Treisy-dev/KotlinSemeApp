import SwiftUI
import shared

struct PromptView: View {
    @StateObject private var viewModel = PromptViewModelWrapper()
    @StateObject private var localizationManager = LocalizationManager.shared
    @State private var promptText = ""
    @State private var showingImagePicker = false
    @State private var showingCamera = false
    @State private var selectedImage: UIImage?
    @State private var imagePath: String? = nil
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {
                    // Image Selection Card
                    VStack(alignment: .leading, spacing: 16) {
                        Text(localizationManager.getString("prompt_add_image"))
                            .font(.title2)
                            .fontWeight(.medium)

                        HStack(spacing: 12) {
                            Button(action: { showingImagePicker = true }) {
                                HStack {
                                    Image(systemName: "photo")
                                    Text(localizationManager.getString("prompt_gallery"))
                                }
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(10)
                            }
                            
                            Button(action: { showingCamera = true }) {
                                HStack {
                                    Image(systemName: "camera")
                                    Text(localizationManager.getString("prompt_camera"))
                                }
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.green)
                                .foregroundColor(.white)
                                .cornerRadius(10)
                            }
                        }
                        
                        // Selected image preview
                        if let image = selectedImage {
                            VStack {
                                Image(uiImage: image)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(maxHeight: 200)
                                    .cornerRadius(10)
                                
                                Button(action: { selectedImage = nil; imagePath = nil }) {
                                    HStack {
                                        Image(systemName: "xmark.circle.fill")
                                        Text(localizationManager.getString("prompt_remove_image"))
                                    }
                                    .foregroundColor(.red)
                                }
                                .padding(.top, 8)
                            }
                        }
                    }
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(12)
                    
                    // Prompt Input Card
                    VStack(alignment: .leading, spacing: 16) {
                        Text(localizationManager.getString("prompt_title"))
                            .font(.title2)
                            .fontWeight(.medium)
                        
                        TextField(localizationManager.getString("prompt_placeholder"), text: $promptText, axis: .vertical)
                            .textFieldStyle(RoundedBorderTextFieldStyle())
                            .lineLimit(3...6)
                    }
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(12)
                    
                    // Send Button
                    Button(action: sendPrompt) {
                        HStack {
                            if viewModel.isLoading {
                                ProgressView()
                                    .scaleEffect(0.8)
                                    .foregroundColor(.white)
                            } else {
                                Image(systemName: "arrow.up.circle.fill")
                            }
                            
                            Text(localizationManager.getString("prompt_send"))
                                .fontWeight(.medium)
                        }
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(promptText.isEmpty || viewModel.isLoading ? Color.gray : Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                    }
                    .disabled(promptText.isEmpty || viewModel.isLoading)
                    
                    // Error display
                    if let error = viewModel.error {
                        Text(error)
                            .font(.caption)
                            .foregroundColor(.red)
                            .padding()
                            .background(Color(.systemRed).opacity(0.1))
                            .cornerRadius(8)
                    }
                }
                .padding()
            }
            .navigationTitle(localizationManager.getString("chat_new_session"))
            .navigationBarTitleDisplayMode(.inline)
        }
        .sheet(isPresented: $showingImagePicker) {
            ImagePicker(selectedImage: $selectedImage, sourceType: .photoLibrary, imagePath: $imagePath)
        }
        .sheet(isPresented: $showingCamera) {
            ImagePicker(selectedImage: $selectedImage, sourceType: .camera, imagePath: $imagePath)
        }
        .onAppear {
            FirebaseAnalyticsManager.shared.trackScreenView(screenName: FirebaseAnalyticsManager.ScreenNames.newChat)
        }
    }
    
    private func sendPrompt() {
        guard !promptText.isEmpty else { return }
        viewModel.sendPrompt(promptText, imagePath: imagePath)
        FirebaseAnalyticsManager.shared.trackMessageSent()
        promptText = ""
        selectedImage = nil
        imagePath = nil
    }
}

struct ImagePicker: UIViewControllerRepresentable {
    @Binding var selectedImage: UIImage?
    let sourceType: UIImagePickerController.SourceType
    @Binding var imagePath: String?
    @Environment(\.dismiss) private var dismiss
    
    func makeUIViewController(context: Context) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.sourceType = sourceType
        picker.delegate = context.coordinator
        return picker
    }
    
    func updateUIViewController(_ uiViewController: UIImagePickerController, context: Context) {}
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    class Coordinator: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
        let parent: ImagePicker
        
        init(_ parent: ImagePicker) {
            self.parent = parent
        }
        
        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
            if let image = info[.originalImage] as? UIImage {
                parent.selectedImage = image
                // Сохраняем изображение во временный файл и передаем путь
                if let data = image.jpegData(compressionQuality: 0.8) {
                    let tempDir = NSTemporaryDirectory()
                    let fileName = UUID().uuidString + ".jpg"
                    let fileURL = URL(fileURLWithPath: tempDir).appendingPathComponent(fileName)
                    try? data.write(to: fileURL)
                    parent.imagePath = fileURL.path
                }
            }
            parent.dismiss()
        }
        
        func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
            parent.dismiss()
        }
    }
}

#Preview {
    PromptView()
} 
