package org.example.project.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.example.project.AppDataBase
import org.example.project.data.datasource.MessageDao
import org.example.project.data.datasource.SessionDao
import org.koin.dsl.module

internal val dataModule = module {
    single<SqlDriver> { AndroidSqliteDriver(AppDataBase.Schema, get(), "seme_app.db") }
    single<AppDataBase> { AppDataBase(get()) }
    factory { MessageDao(get()) }
    factory { SessionDao(get()) }
}
