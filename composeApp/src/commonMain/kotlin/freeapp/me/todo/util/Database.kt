package freeapp.me.todo.util

import androidx.room.RoomDatabase
import androidx.room.Transactor
import androidx.room.useReaderConnection
import androidx.room.useWriterConnection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext


suspend fun <R> RoomDatabase.withTransaction(
    dispatcher: CoroutineDispatcher = Dispatchers.IO, // ⭐️ 트랜잭션을 실행할 Dispatcher ⭐️
    block: suspend RoomDatabase.() -> R // RoomDatabase 컨텍스트에서 실행되는 람다
): R {

    // ⭐️ `this@withTransaction`은 RoomDatabase 인스턴스입니다.
    // 이 인스턴스를 capture하여 `Transactor.withTransaction` 람다 안에서 사용합니다.
    val roomDatabaseInstance = this@withTransaction

    return withContext(dispatcher) {
        useWriterConnection { transactor ->
            transactor.withTransaction(Transactor.SQLiteTransactionType.DEFERRED) {
                block(roomDatabaseInstance)
            }
        }
    }
}
