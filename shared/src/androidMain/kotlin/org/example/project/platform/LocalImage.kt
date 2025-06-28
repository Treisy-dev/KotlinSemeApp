package org.example.project.platform

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import java.io.File

@Composable
actual fun LocalImage(
    path: String,
    modifier: Modifier,
    contentDescription: String?
) {
    println("LocalImage: Loading image from path: $path")
    val file = File(path)
    if (file.exists()) {
        println("LocalImage: File exists, size: ${file.length()} bytes")
        runCatching {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                println("LocalImage: Successfully decoded bitmap: ${bitmap.width}x${bitmap.height}")
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = contentDescription,
                    modifier = modifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                println("LocalImage: Failed to decode bitmap")
                showErrorPlaceholder(modifier, contentDescription)
            }
        }.onFailure { error ->
            println("LocalImage: Error loading image: $error")
            error.printStackTrace()
            showErrorPlaceholder(modifier, contentDescription)
        }
    } else {
        println("LocalImage: File does not exist: $path")
        showErrorPlaceholder(modifier, contentDescription)
    }
}

@Composable
private fun showErrorPlaceholder(modifier: Modifier, contentDescription: String?) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.BrokenImage,
            contentDescription = contentDescription ?: "Failed to load image",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxSize()
        )
    }
} 