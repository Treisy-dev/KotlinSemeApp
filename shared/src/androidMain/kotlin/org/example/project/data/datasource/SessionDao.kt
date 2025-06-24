package org.example.project.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.project.AppDataBase
import org.example.project.Session

class SessionDao(
    private val dataBase: AppDataBase
) {

    fun getAll(): Flow<List<Session>> {
        return flow {
            dataBase.sessionQueries
                .getAll()
                .executeAsList()
        }
    }

    fun insert(session: Session) {
        dataBase.sessionQueries.insert(session)
    }

    fun update(id: String, lastMessage: String, hasImage: Boolean) {
        val hasImageField = if (hasImage) 1L else 0L
        dataBase.sessionQueries.updateById(lastMessage, hasImageField, id)
    }

    fun deleteById(id: String) {
        dataBase.sessionQueries.deleteById(id)
    }

    fun deleteAll() {
        dataBase.sessionQueries.deleteAll()
    }
}
