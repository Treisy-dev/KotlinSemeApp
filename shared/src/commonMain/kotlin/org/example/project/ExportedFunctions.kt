package org.example.project

import org.example.project.screens.ChatViewModel
import org.example.project.screens.SettingsViewModel
import org.example.project.screens.PromptViewModel
import org.example.project.screens.HistoryViewModel
import org.koin.mp.KoinPlatform
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
object ExportedFunctions {
    @Throws(Exception::class)
    fun createChatViewModel(): ChatViewModel = KoinPlatform.getKoin().get()

    @Throws(Exception::class)
    fun createPromptViewModel(): PromptViewModel = KoinPlatform.getKoin().get()

    @Throws(Exception::class)
    fun createHistoryViewModel(): HistoryViewModel = KoinPlatform.getKoin().get()

    @Throws(Exception::class)
    fun createSettingsViewModel(): SettingsViewModel = KoinPlatform.getKoin().get()
} 