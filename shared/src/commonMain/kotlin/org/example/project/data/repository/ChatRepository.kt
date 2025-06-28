package org.example.project.data.repository

import org.example.project.data.local.ChatDatabase
import org.example.project.data.model.ChatMessage
import org.example.project.data.model.ChatSession
import kotlinx.coroutines.flow.Flow
import com.benasher44.uuid.uuid4

class ChatRepository(
    private val database: ChatDatabase,
    private val geminiRepository: GeminiRepository
) {
    fun getMessages(sessionId: String): Flow<List<ChatMessage>> {
        return database.getMessages(sessionId)
    }
    
    fun getAllSessions(): Flow<List<ChatSession>> {
        return database.getAllSessions()
    }
    
    suspend fun sendMessage(
        sessionId: String,
        content: String,
        imagePath: String? = null
    ): Result<ChatMessage> {
        return try {
            // Save user message
            val userMessage = ChatMessage(
                id = generateId(),
                content = content,
                imagePath = imagePath,
                isUser = true,
                timestamp = kotlinx.datetime.Clock.System.now(),
                sessionId = sessionId
            )
            database.insertMessage(userMessage)
            
            // Get AI response
            val response = geminiRepository.sendMessage(content, imagePath)
            
            // Save AI response
            val aiMessage = ChatMessage(
                id = generateId(),
                content = response.text,
                isUser = false,
                timestamp = kotlinx.datetime.Clock.System.now(),
                sessionId = sessionId
            )
            database.insertMessage(aiMessage)
            
            // Update session
            database.updateSession(sessionId, content, hasImage = imagePath != null)
            
            Result.success(aiMessage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createSession(title: String): String {
        val sessionId = generateId()
        database.createSession(sessionId, title)
        return sessionId
    }
    
    suspend fun deleteSession(sessionId: String) {
        database.deleteSession(sessionId)
    }
    
    suspend fun clearHistory() {
        database.clearAll()
    }
    
    private fun generateId(): String {
        return uuid4().toString()
    }
} 