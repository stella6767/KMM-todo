package freeapp.me.todo.config.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import freeapp.me.todo.data.repository.TodoDao
import freeapp.me.todo.domain.model.Todo


@Database(entities = [Todo::class], version = 1)
@TypeConverters(InstantConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    suspend fun performTransaction(todoDao: TodoDao, newTodos: List<Todo>) {

    }

    companion object {
        const val DB_FILE_NAME = "todo.db"
    }
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

