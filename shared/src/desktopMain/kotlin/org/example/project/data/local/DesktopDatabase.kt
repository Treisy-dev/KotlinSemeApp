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
import java.io.File

class DesktopDatabase : ChatDatabase {
    private val dbFile = File(System.getProperty("user.home"), ".semeapp_chat.db.json")
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        serializersModule = kotlinx.serialization.modules.SerializersModule {
            contextual(Instant::class, InstantSerializer)
        }
    }
    
    private var database: DatabaseModel
    
    private val messagesFlow = MutableStateFlow<List<ChatMessage>>(emptyList())
    private val sessionsFlow = MutableStateFlow<List<ChatSession>>(emptyList())

    init {
        database = loadDatabase()
        messagesFlow.value = database.messages
        sessionsFlow.value = database.sessions
        println("DesktopDatabase initialized with ${database.sessions.size} sessions and ${database.messages.size} messages")
    }

    private fun loadDatabase(): DatabaseModel {
        return if (dbFile.exists()) {
            try {
                val content = dbFile.readText()
                println("Loading database from: ${dbFile.absolutePath}")
                val result = json.decodeFromString<DatabaseModel>(content)
                println("Loaded ${result.sessions.size} sessions and ${result.messages.size} messages")
                result
            } catch (e: Exception) {
                println("Error reading database, creating new one: ${e.message}")
                e.printStackTrace()
                DatabaseModel()
            }
        } else {
            println("Database file not found, creating new one")
            DatabaseModel()
        }
    }

    private fun saveDatabase() {
        try {
            val content = json.encodeToString(database)
            dbFile.writeText(content)
            println("Database saved to: ${dbFile.absolutePath}")
        } catch (e: Exception) {
            println("Error saving database: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun getMessages(sessionId: String): Flow<List<ChatMessage>> {
        return messagesFlow.asStateFlow().map { messages ->
            messages.filter { it.sessionId == sessionId }
        }
    }

    override fun getAllSessions(): Flow<List<ChatSession>> {
        return sessionsFlow.asStateFlow()
    }
    
    override fun getAllMessages(): Flow<List<ChatMessage>> {
        return messagesFlow.asStateFlow()
    }

    override suspend fun insertMessage(message: ChatMessage) {
        println("Inserting message: ${message.content.take(50)}...")
        database = database.copy(messages = database.messages + message)
        messagesFlow.value = database.messages
        saveDatabase()
    }

    override suspend fun createSession(sessionId: String, title: String) {
        println("Creating session: $title")
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
        println("Updating session: $sessionId with message: ${lastMessage.take(50)}...")
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
        println("Deleting session: $sessionId")
        database = database.copy(
            sessions = database.sessions.filterNot { it.id == sessionId },
            messages = database.messages.filterNot { it.sessionId == sessionId }
        )
        sessionsFlow.value = database.sessions
        messagesFlow.value = database.messages
        saveDatabase()
    }

    override suspend fun clearAll() {
        println("Clearing all data")
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
    
    object InstantSerializer : KSerializer<Instant> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)
        
        override fun serialize(encoder: Encoder, value: Instant) {
            encoder.encodeString(value.toString())
        }
        
        override fun deserialize(decoder: Decoder): Instant {
            return Instant.parse(decoder.decodeString())
        }
    }
} 