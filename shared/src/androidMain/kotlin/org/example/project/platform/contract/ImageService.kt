package org.example.project.platform.contract

import android.net.Uri

interface ImageService {

    suspend fun chooseImage(): Uri?
}
