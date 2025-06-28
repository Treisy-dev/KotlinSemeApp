package org.example.project.platform.contract

import android.net.Uri

interface CameraService {

    suspend fun takePhoto(): Uri?
}
