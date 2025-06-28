package org.example.project.ui

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.example.project.App
import org.example.project.platform.contract.CameraService
import org.example.project.platform.contract.ImageService

class MainActivity : ComponentActivity(), CameraService, ImageService {

    private val recentPhotoStatuses: Channel<Boolean> = Channel()
    private val recentImages: Channel<Uri?> = Channel()

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        lifecycleScope.launch {
            recentPhotoStatuses.send(isSuccess)
        }
    }

    private val chooseImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        lifecycleScope.launch {
            recentImages.send(uri)
        }
    }

    override suspend fun takePhoto(): Uri? {
        val imageUri = createImageUri()
        takePhotoLauncher.launch(imageUri)
        return if (recentPhotoStatuses.receive()) {
            imageUri
        } else {
            null
        }
    }

    override suspend fun chooseImage(): Uri? {
        chooseImageLauncher.launch("image/*")
        return recentImages.receive()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivity = this
        setContent {
            App()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentActivity = null
    }

    private fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver
            ?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw IllegalStateException("Failed to create image uri")
    }

    companion object {

        private var currentActivity: MainActivity? = null

        fun provideCameraService(): CameraService? = currentActivity

        fun provideImageService(): ImageService? = currentActivity
    }
}
