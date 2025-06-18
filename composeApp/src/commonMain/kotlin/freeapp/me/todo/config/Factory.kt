package freeapp.me.todo.config

import freeapp.me.todo.model.network.TodoApi
import freeapp.me.todo.model.network.TodoApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect class Factory {
    fun createRoomDatabase(): AppDatabase
    fun createApi(): TodoApi
}

internal fun commonCreateApi(): TodoApi =
    TodoApiImpl(
        client = HttpClient {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Any)
            }
        },
    )

val json = Json { ignoreUnknownKeys = true }
