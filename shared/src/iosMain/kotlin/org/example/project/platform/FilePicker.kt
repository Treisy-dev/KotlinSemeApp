package org.example.project.platform

import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class FilePicker(private val rootController: UIViewController) {
    actual suspend fun pickImage(): String? {
        // iOS implementation for picking an image
        return null
    }

    actual suspend fun pickFile(extensions: List<String>): String? {
        // iOS implementation for picking a file
        return null
    }
} 