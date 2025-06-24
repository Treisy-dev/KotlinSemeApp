package org.example.project.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.project.AppDataBase
import org.example.project.Message

class MessageDao(
    private val dataBase: AppDataBase
) {

    fun getAll(): Flow<List<Message>> {
        return flow {
            dataBase.messageQueries
                .getAll()
                .executeAsList()
        }
    }

    fun getBySessionId(sessionId: String): Flow<List<Message>> {
        return flow {
            dataBase.messageQueries
                .getBySessionId(sessionId)
                .executeAsOne()
        }
    }

    fun insert(message: Message) {
        dataBase.messageQueries.insert(message)
    }

    fun deleteAll() {
        dataBase.messageQueries.deleteAll()
    }
}
