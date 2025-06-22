package org.example.project.platform

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.skia.Image
import java.io.File

@Composable
actual fun LocalImage(
    path: String,
    modifier: Modifier,
    contentDescription: String?
) {
    val file = File(path)
    if (file.exists()) {
        runCatching {
            val imageBitmap = Image.makeFromEncoded(file.readBytes()).toComposeImageBitmap()
            Image(
                bitmap = imageBitmap,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }.onFailure {
            // Handle image loading error, maybe show a placeholder
            println("Error loading image: $it")
        }
    }
} 