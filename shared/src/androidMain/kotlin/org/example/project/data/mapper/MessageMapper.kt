package org.example.project.data.mapper

import kotlinx.datetime.Instant
import org.example.project.Message
import org.example.project.data.model.ChatMessage

internal fun Message.toChatMessage(): ChatMessage =
    ChatMessage(
        id = id,
        content = content,
        imagePath = imagePath,
        isUser = isUser > 0,
        timestamp = Instant.fromEpochMilliseconds(timestamp),
        sessionId = sessionId
    )

internal fun ChatMessage.toMessage(): Message =
    Message(
        id = id,
        content = content,
        imagePath = imagePath,
        isUser = if (isUser) 1L else 0L,
        timestamp = timestamp.toEpochMilliseconds(),
        sessionId = sessionId
    )
