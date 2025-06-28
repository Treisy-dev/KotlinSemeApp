package org.example.project.data.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.example.project.AppDataBase
import org.example.project.Message

class MessageDao(
    private val dataBase: AppDataBase,
    private val dispatcher: CoroutineDispatcher
) {

    fun getAll(): Flow<List<Message>> {
        return dataBase.messageQueries
            .getAll()
            .asFlow()
            .mapToList(dispatcher)
    }

    fun getBySessionId(sessionId: String): Flow<List<Message>> {
        return dataBase.messageQueries
            .getBySessionId(sessionId)
            .asFlow()
            .mapToList(dispatcher)
    }

    fun insert(message: Message) {
        dataBase.messageQueries.insert(message)
    }

    fun deleteAll() {
        dataBase.messageQueries.deleteAll()
    }
}
