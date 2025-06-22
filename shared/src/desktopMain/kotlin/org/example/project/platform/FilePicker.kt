package org.example.project.platform

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

actual class FilePicker {
    actual suspend fun pickImage(): String? = withContext(Dispatchers.IO) {
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Select Image"
        fileChooser.fileFilter = FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif", "bmp", "webp"
        )

        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile.absolutePath
        } else {
            null
        }
    }

    actual suspend fun pickFile(extensions: List<String>): String? = withContext(Dispatchers.IO) {
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Select File"
        if (extensions.isNotEmpty()) {
            fileChooser.fileFilter = FileNameExtensionFilter(
                "${extensions.joinToString(", ").uppercase()} files", *extensions.toTypedArray()
            )
        }

        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile.absolutePath
        } else {
            null
        }
    }
} 