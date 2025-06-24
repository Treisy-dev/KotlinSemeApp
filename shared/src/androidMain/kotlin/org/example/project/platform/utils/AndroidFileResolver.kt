package org.example.project.platform.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class AndroidFileResolver(
    private val context: Context
) {

    fun getRealPathFromUri(uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        if (uri.scheme == "content") {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
            }
        }
        else if (uri.scheme == "file") {
            return uri.path
        }
        return null
    }
}
