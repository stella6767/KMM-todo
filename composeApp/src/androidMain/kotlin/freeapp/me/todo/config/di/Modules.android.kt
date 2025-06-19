package freeapp.me.todo.config.di

import freeapp.me.todo.config.db.AppDatabase
import freeapp.me.todo.config.db.DatabaseFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module


actual val platformModule: Module
    get() = module {

        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory(androidApplication()) }
    }
