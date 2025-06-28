package org.example.project

import org.example.project.di.initKoin_iOS
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
object ExportedFunctionsIOS {
    @Throws(Exception::class)
    fun initializeKoin() {
        initKoin_iOS()
    }
} 