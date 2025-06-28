package org.example.project.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidShareSheet(
    private val context: Context
) : ShareSheet {

    private val clipboardManager = context
        .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    override suspend fun shareText(text: String, title: String) {
        try {
            val uriText = Uri.encode(text)
            val uriTitle = Uri.encode(title)
            val mailto = "mailto:?subject=$uriTitle&body=$uriText"

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(mailto)
            }

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(Intent.createChooser(intent, "Отправить email через"))
            } else {
                copyToClipboard(text)
            }
        } catch (e: Exception) {
            println("Error sharing text: ${e.message}")
        }
    }

    override suspend fun shareImage(imagePath: String, text: String) {
        try {
            val imageUri = Uri.parse(imagePath)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(imageUri, "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Открыть изображение с помощью"))
        } catch (e: Exception) {
            println("Error sharing image: ${e.message}")
        }
    }

    override suspend fun copyToClipboard(text: String): Boolean {
        val clipData = ClipData.newPlainText(LABEL_MESSAGE, text)
        clipboardManager.setPrimaryClip(clipData)
        return true
    }

    companion object {

        const val LABEL_MESSAGE = "message"
    }
}
