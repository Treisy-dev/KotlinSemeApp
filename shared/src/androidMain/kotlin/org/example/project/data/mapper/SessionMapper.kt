package org.example.project.data.mapper

import kotlinx.datetime.Instant
import org.example.project.Session
import org.example.project.data.model.ChatSession

internal fun Session.toChatSession(): ChatSession =
    ChatSession(
        id = id,
        title = title,
        lastMessage = lastMessage,
        timestamp = Instant.fromEpochMilliseconds(timestamp),
        hasImage = hasImage > 0L
    )

internal fun ChatSession.toSession(): Session =
    Session(
        id = id,
        title = title,
        lastMessage = lastMessage,
        timestamp = timestamp.toEpochMilliseconds(),
        hasImage = if (hasImage) 1L else 0L
    )
