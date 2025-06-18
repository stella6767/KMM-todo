package freeapp.me.todo.config.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context,
) {
    actual fun create(): RoomDatabase.Builder<AppDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(AppDatabase.DB_FILE_NAME)
        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}
