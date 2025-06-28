package org.example.project.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.example.project.data.model.ChatMessage
import org.example.project.data.model.ChatSession
import kotlinx.datetime.Instant
import platform.Foundation.*
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class IOSDatabase : ChatDatabase {
    private val dbFile: String = run {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, true
        )
        val dir = paths.firstOrNull() ?: NSHomeDirectory()
        "$dir/semeapp_chat.db.json"
    }
    
    
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private var database: DatabaseModel
    private val messagesFlow = MutableStateFlow<List<ChatMessage>>(emptyList())
    private val sessionsFlow = MutableStateFlow<List<ChatSession>>(emptyList())

    init {
        database = loadDatabase()
        messagesFlow.value = database.messages
        sessionsFlow.value = database.sessions
    }

    private fun loadDatabase(): DatabaseModel {
        val fileManager = NSFileManager.defaultManager
        return if (fileManager.fileExistsAtPath(dbFile)) {
            val data = NSData.dataWithContentsOfFile(dbFile)
            val content = data?.let { NSString.create(it, NSUTF8StringEncoding) }
            if (content != null) {
                json.decodeFromString(DatabaseModel.serializer(), content.toString())
            } else {
                DatabaseModel()
            }
        } else {
            DatabaseModel()
        }
    }

    private fun saveDatabase() {
        try {
            val content = json.encodeToString(database)
            val ns = NSString.create(string = content)
            ns.writeToFile(dbFile, atomically = true, encoding = NSUTF8StringEncoding, error = null)
        } catch (_: Exception) {}
    }

    override fun getMessages(sessionId: String): Flow<List<ChatMessage>> {
        return messagesFlow.asStateFlow().map { messages ->
            messages.filter { it.sessionId == sessionId }
        }
    }

    override fun getAllSessions(): Flow<List<ChatSession>> = sessionsFlow.asStateFlow()
    override fun getAllMessages(): Flow<List<ChatMessage>> = messagesFlow.asStateFlow()

    override suspend fun insertMessage(message: ChatMessage) {
        database = database.copy(messages = database.messages + message)
        messagesFlow.value = database.messages
        saveDatabase()
    }

    override suspend fun createSession(sessionId: String, title: String) {
        val session = ChatSession(
            id = sessionId,
            title = title,
            lastMessage = "",
            timestamp = kotlinx.datetime.Clock.System.now()
        )
        database = database.copy(sessions = database.sessions + session)
        sessionsFlow.value = database.sessions
        saveDatabase()
    }

    override suspend fun updateSession(sessionId: String, lastMessage: String, hasImage: Boolean) {
        val index = database.sessions.indexOfFirst { it.id == sessionId }
        if (index != -1) {
            val updatedSession = database.sessions[index].copy(
                lastMessage = lastMessage,
                timestamp = kotlinx.datetime.Clock.System.now(),
                hasImage = hasImage
            )
            database = database.copy(sessions = database.sessions.toMutableList().apply { set(index, updatedSession) })
            sessionsFlow.value = database.sessions
            saveDatabase()
        }
    }

    override suspend fun deleteSession(sessionId: String) {
        database = database.copy(
            sessions = database.sessions.filterNot { it.id == sessionId },
            messages = database.messages.filterNot { it.sessionId == sessionId }
        )
        sessionsFlow.value = database.sessions
        messagesFlow.value = database.messages
        saveDatabase()
    }

    override suspend fun clearAll() {
        database = DatabaseModel()
        sessionsFlow.value = emptyList()
        messagesFlow.value = emptyList()
        saveDatabase()
    }

    @kotlinx.serialization.Serializable
    private data class DatabaseModel(
        val sessions: List<ChatSession> = emptyList(),
        val messages: List<ChatMessage> = emptyList()
    )
} 
