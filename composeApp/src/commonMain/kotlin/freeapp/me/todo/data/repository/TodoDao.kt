package freeapp.me.todo.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import freeapp.me.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(todo: Todo) : Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(todos: List<Todo>)

    @Query("SELECT * FROM Todo")
    fun getAllAsFlow(): Flow<List<Todo>>

    @Query("SELECT * FROM Todo ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getTodosPaginated(limit: Int, offset: Int): List<Todo>


    @Query("SELECT COUNT(*) as count FROM Todo")
    suspend fun count(): Int

    @Query("SELECT * FROM Todo WHERE id in (:ids)")
    suspend fun loadAll(ids: List<Long>): List<Todo>

    @Query("SELECT * FROM Todo WHERE id = :id")
    suspend fun getById(id: Long): Todo?

    @Query("SELECT * FROM Todo WHERE id in (:ids)")
    suspend fun loadMapped(ids: List<Long>): Map<@MapColumn(columnName = "id") Long, Todo, >

    @Query("DELETE FROM Todo WHERE id = :id")
    suspend fun deleteById(id: Long) : Int // 삭제된 row 수 반환 (옵션)

    // ⭐️ 추가: 모든 Todo를 삭제하는 메서드
    @Query("DELETE FROM Todo")
    suspend fun deleteAll()

}
