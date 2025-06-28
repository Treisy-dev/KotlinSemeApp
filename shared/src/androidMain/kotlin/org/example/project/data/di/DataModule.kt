package org.example.project.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import kotlinx.coroutines.Dispatchers
import org.example.project.AppDataBase
import org.example.project.data.dao.MessageDao
import org.example.project.data.dao.SessionDao
import org.koin.dsl.module

internal val dataModule = module {
    single<SqlDriver> { AndroidSqliteDriver(AppDataBase.Schema, get(), "seme_app.db") }
    single<AppDataBase> { AppDataBase(get()) }
    factory { MessageDao(get(), Dispatchers.IO) }
    factory { SessionDao(get(), Dispatchers.IO) }
}
