package org.example.project

import org.example.project.screens.ChatViewModel
import org.example.project.screens.SettingsViewModel
import org.example.project.screens.PromptViewModel
import org.example.project.screens.HistoryViewModel
import org.koin.mp.KoinPlatform

// Экспортируемые функции для Swift/ObjC
@kotlin.jvm.JvmName("GetChatViewModel")
fun GetChatViewModel(): ChatViewModel = KoinPlatform.getKoin().get()

@kotlin.jvm.JvmName("GetPromptViewModel")
fun GetPromptViewModel(): PromptViewModel = KoinPlatform.getKoin().get()

@kotlin.jvm.JvmName("GetHistoryViewModel")
fun GetHistoryViewModel(): HistoryViewModel = KoinPlatform.getKoin().get()

@kotlin.jvm.JvmName("GetSettingsViewModel")
fun GetSettingsViewModel(): SettingsViewModel = KoinPlatform.getKoin().get() 