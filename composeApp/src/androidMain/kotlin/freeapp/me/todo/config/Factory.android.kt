package freeapp.me.todo.config

import android.app.Application
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import freeapp.me.todo.model.network.TodoApi
import kotlinx.coroutines.Dispatchers

actual class Factory(
    private val app: Application,
) {

    actual fun createRoomDatabase(): AppDatabase {
        val dbFile = app.getDatabasePath(DB_FILE_NAME)

        return Room
            .databaseBuilder<AppDatabase>(
                context = app,
                name = dbFile.absolutePath,
            ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    actual fun createApi(): TodoApi {
        return commonCreateApi()
    }
}
