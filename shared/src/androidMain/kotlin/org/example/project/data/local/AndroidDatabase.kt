package org.example.project.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.Session
import org.example.project.data.datasource.MessageDao
import org.example.project.data.datasource.SessionDao
import org.example.project.data.mapper.toChatMessage
import org.example.project.data.mapper.toChatSession
import org.example.project.data.mapper.toMessage
import org.example.project.data.model.ChatMessage
import org.example.project.data.model.ChatSession

class AndroidDatabase(
    private val messageDao: MessageDao,
    private val sessionDao: SessionDao
) : ChatDatabase {

    override fun getMessages(sessionId: String): Flow<List<ChatMessage>> {
        return messageDao
            .getBySessionId(sessionId)
            .map { messages ->
                messages.map { message ->
                    message.toChatMessage()
                }
            }
    }

    override fun getAllSessions(): Flow<List<ChatSession>> {
        return sessionDao
            .getAll()
            .map { sessions ->
                sessions.map { session ->
                    session.toChatSession()
                }
            }
    }

    override fun getAllMessages(): Flow<List<ChatMessage>> {
        return messageDao
            .getAll()
            .map { messages ->
                messages.map { message ->
                    message.toChatMessage()
                }
            }
    }

    override suspend fun insertMessage(message: ChatMessage) {
        messageDao.insert(message.toMessage())
    }

    override suspend fun createSession(sessionId: String, title: String) {
        val session = Session(
            id = sessionId,
            timestamp = System.currentTimeMillis(),
            title = title,
            hasImage = 0L,
            lastMessage = ""
        )
        sessionDao.insert(session)
    }

    override suspend fun updateSession(sessionId: String, lastMessage: String, hasImage: Boolean) {
        sessionDao.update(
            id = sessionId,
            lastMessage = lastMessage,
            hasImage = hasImage
        )
    }

    override suspend fun deleteSession(sessionId: String) {
        sessionDao.deleteById(sessionId)
    }

    override suspend fun clearAll() {
        messageDao.deleteAll()
        sessionDao.getAll()
    }
}
