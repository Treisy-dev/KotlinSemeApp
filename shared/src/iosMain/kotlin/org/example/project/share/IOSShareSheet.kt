package org.example.project.share

import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSArray
import platform.Foundation.NSMutableArray
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIPasteboard
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import platform.UIKit.UIImage
import platform.Foundation.NSURL
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile
import platform.UIKit.UIImagePNGRepresentation
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class IOSShareSheet : ShareSheet {
    override suspend fun shareText(text: String, title: String) {
        withContext(Dispatchers.Main) {
            val activityItems = listOf(text)
            presentActivityController(activityItems)
        }
    }

    override suspend fun shareImage(imagePath: String, text: String) {
        withContext(Dispatchers.Main) {
            val image = UIImage.imageWithContentsOfFile(imagePath)
            val items = if (image != null) listOf(image, text) else listOf(text)
            presentActivityController(items)
        }
    }

    override suspend fun copyToClipboard(text: String): Boolean = withContext(Dispatchers.Main) {
        UIPasteboard.generalPasteboard.string = text
        true
    }

    private fun presentActivityController(items: List<Any?>) {
        val nonNull = items.filterNotNull()
        val controller = UIActivityViewController(
            activityItems = nonNull,
            applicationActivities = null
        )
        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootVC?.presentViewController(controller, animated = true, completion = null)
    }
}

@OptIn(ExperimentalForeignApi::class)
fun List<Any?>.toNSArray(): NSArray {
    val array = NSMutableArray()
    for (item in this) {
        array.addObject(item)
    }
    return array
} 