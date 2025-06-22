import SwiftUI
import shared

struct PromptView: View {
    @StateObject private var viewModel = PromptViewModel()
    @State private var promptText = ""
    @State private var showingImagePicker = false
    @State private var showingCamera = false
    @State private var selectedImage: UIImage?
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {
                    // Image Selection Card
                    VStack(alignment: .leading, spacing: 16) {
                        Text("Add Image (Optional)")
                            .font(.title2)
                            .fontWeight(.medium)
                        
                        HStack(spacing: 12) {
                            Button(action: { showingImagePicker = true }) {
                                HStack {
                                    Image(systemName: "photo")
                                    Text("Gallery")
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
                                    Text("Camera")
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
                                
                                Button(action: { selectedImage = nil }) {
                                    HStack {
                                        Image(systemName: "xmark.circle.fill")
                                        Text("Remove Image")
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
                        Text("Your Prompt")
                            .font(.title2)
                            .fontWeight(.medium)
                        
                        TextField("Describe what you want to know or ask...", text: $promptText, axis: .vertical)
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
                            
                            Text("Send to Gemini")
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
            .navigationTitle("New Chat")
            .navigationBarTitleDisplayMode(.inline)
        }
        .sheet(isPresented: $showingImagePicker) {
            ImagePicker(selectedImage: $selectedImage, sourceType: .photoLibrary)
        }
        .sheet(isPresented: $showingCamera) {
            ImagePicker(selectedImage: $selectedImage, sourceType: .camera)
        }
    }
    
    private func sendPrompt() {
        guard !promptText.isEmpty else { return }
        viewModel.sendPrompt(promptText, imagePath: nil) // TODO: Handle image path
        promptText = ""
        selectedImage = nil
    }
}

struct ImagePicker: UIViewControllerRepresentable {
    @Binding var selectedImage: UIImage?
    let sourceType: UIImagePickerController.SourceType
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