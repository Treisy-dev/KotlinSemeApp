package org.example.project.data.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.example.project.AppDataBase
import org.example.project.Session

class SessionDao(
    private val dataBase: AppDataBase,
    private val dispatcher: CoroutineDispatcher
) {

    fun getAll(): Flow<List<Session>> {
        return dataBase.sessionQueries
            .getAll()
            .asFlow()
            .mapToList(dispatcher)
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
