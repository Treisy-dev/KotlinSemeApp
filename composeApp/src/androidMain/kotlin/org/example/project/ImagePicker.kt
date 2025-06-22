package org.example.project

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun rememberImagePicker(
    onImageSelected: (String?) -> Unit
): ImagePicker {
    val context = LocalContext.current
    
    var cameraFile by remember { mutableStateOf<File?>(null) }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        println("Gallery launcher result: $uri")
        uri?.let { selectedUri ->
            try {
                // Create a temporary file
                val tempFile = File.createTempFile("image_", ".jpg", context.cacheDir)
                println("Created temp file: ${tempFile.absolutePath}")
                
                // Copy the selected image to the temporary file
                context.contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                    FileOutputStream(tempFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                
                println("Successfully copied image to: ${tempFile.absolutePath}")
                onImageSelected(tempFile.absolutePath)
            } catch (e: Exception) {
                println("Error copying image: ${e.message}")
                e.printStackTrace()
                onImageSelected(null)
            }
        } ?: run {
            println("No image selected from gallery")
            onImageSelected(null)
        }
    }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        println("Camera launcher result: success=$success, file=${cameraFile?.absolutePath}")
        if (success && cameraFile != null) {
            onImageSelected(cameraFile!!.absolutePath)
        } else {
            println("Camera failed or cancelled")
            onImageSelected(null)
        }
        cameraFile = null
    }
    
    return remember {
        ImagePicker(
            pickFromGallery = { 
                println("Launching gallery picker")
                galleryLauncher.launch("image/*") 
            },
            takePhoto = {
                try {
                    // Create a temporary file for camera
                    val tempFile = File.createTempFile("photo_", ".jpg", context.cacheDir)
                    cameraFile = tempFile
                    val fileUri = androidx.core.net.Uri.fromFile(tempFile)
                    println("Launching camera with file: ${tempFile.absolutePath}")
                    cameraLauncher.launch(fileUri)
                } catch (e: Exception) {
                    println("Error setting up camera: ${e.message}")
                    e.printStackTrace()
                    onImageSelected(null)
                }
            }
        )
    }
}

class ImagePicker(
    val pickFromGallery: () -> Unit,
    val takePhoto: () -> Unit
) 