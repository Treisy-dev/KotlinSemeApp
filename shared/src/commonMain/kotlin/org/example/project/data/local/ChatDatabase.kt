package org.example.project.data.local

import org.example.project.data.model.ChatMessage
import org.example.project.data.model.ChatSession
import kotlinx.coroutines.flow.Flow

interface ChatDatabase {
    fun getMessages(sessionId: String): Flow<List<ChatMessage>>
    fun getAllSessions(): Flow<List<ChatSession>>
    suspend fun insertMessage(message: ChatMessage)
    suspend fun createSession(sessionId: String, title: String)
    suspend fun updateSession(sessionId: String, lastMessage: String, hasImage: Boolean)
    suspend fun deleteSession(sessionId: String)
    suspend fun clearAll()
}

class ChatDatabaseImpl : ChatDatabase {
    private val messages = mutableListOf<ChatMessage>()
    private val sessions = mutableListOf<ChatSession>()
    
    override fun getMessages(sessionId: String): Flow<List<ChatMessage>> {
        return kotlinx.coroutines.flow.flowOf(
            messages.filter { it.sessionId == sessionId }
        )
    }
    
    override fun getAllSessions(): Flow<List<ChatSession>> {
        return kotlinx.coroutines.flow.flowOf(sessions)
    }
    
    override suspend fun insertMessage(message: ChatMessage) {
        messages.add(message)
    }
    
    override suspend fun createSession(sessionId: String, title: String) {
        val session = ChatSession(
            id = sessionId,
            title = title,
            lastMessage = "",
            timestamp = kotlinx.datetime.Clock.System.now()
        )
        sessions.add(session)
    }
    
    override suspend fun updateSession(sessionId: String, lastMessage: String, hasImage: Boolean) {
        val index = sessions.indexOfFirst { it.id == sessionId }
        if (index != -1) {
            sessions[index] = sessions[index].copy(
                lastMessage = lastMessage,
                timestamp = kotlinx.datetime.Clock.System.now(),
                hasImage = hasImage
            )
        }
    }
    
    override suspend fun deleteSession(sessionId: String) {
        sessions.removeAll { it.id == sessionId }
        messages.removeAll { it.sessionId == sessionId }
    }
    
    override suspend fun clearAll() {
        messages.clear()
        sessions.clear()
    }
} 