package org.example.project.platform

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile
import platform.posix.memcpy

@Composable
actual fun LocalImage(
    path: String,
    modifier: Modifier,
    contentDescription: String?
) {
    NSData.dataWithContentsOfFile(path)?.let { data ->
        runCatching {
            val imageBitmap = Image.makeFromEncoded(data.toByteArray()).toComposeImageBitmap()
            Image(
                bitmap = imageBitmap,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }.onFailure {
            println("Error loading image: $it")
        }
    }
}

private fun NSData.toByteArray(): ByteArray {
    val length = this.length.toLong()
    if (length == 0L) return ByteArray(0)
    val bytes = ByteArray(length.toInt())
    bytes.usePinned {
        memcpy(it.addressOf(0), this.bytes, this.length)
    }
    return bytes
} 