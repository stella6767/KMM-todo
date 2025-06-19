package freeapp.me.todo.model.repository

import freeapp.me.todo.config.db.AppDatabase
import freeapp.me.todo.model.data.PageImpl
import freeapp.me.todo.model.data.Todo
import freeapp.me.todo.util.Logger
import freeapp.me.todo.util.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class TodoRoomRepositoryImpl(
    private val database: AppDatabase,
) : TodoRepository {

    val todoDao = database.todoDao()


    override fun getTodos(): Flow<List<Todo>> {
        return todoDao.getAllAsFlow()
    }

    override suspend fun getTodosPaginated(
        currentPage: Int,
        pageSize: Int
    ): List<Todo> {
        val offset = (currentPage - 1) * pageSize

        return todoDao.getTodosPaginated(pageSize, offset) // 페이지 데이터 Flow
    }

    override suspend fun getTodoById(id: Long): Todo? {
        return todoDao.getById(id)
    }

    override suspend fun insertTodo(todo: Todo): Todo {
        return database.withTransaction {
            val insertedId = todoDao.insert(todo)
            getTodoById(insertedId) ?: throw NoSuchElementException("Todo with ID $insertedId not found.")
        }
    }

    override suspend fun deleteTodoById(id: Long): Boolean {
        val deleteById = todoDao.deleteById(id)
        return deleteById !== 0
    }

    override suspend fun toggleTodoStatus(id: Long): Todo {

        return database.withTransaction {
            val todo = getTodoById(id)
            if (todo != null) {
                val updatedTodo = todo.copy(isDone = !todo.isDone)
                todoDao.insert(updatedTodo) // REPLACE 전략이므로 업데이트 역할을 합니다.
                updatedTodo
            } else {
                throw NoSuchElementException("Todo with ID $id not found.")
            }
        }

    }

    override suspend fun deleteAllTodos() {

        todoDao.deleteAll()
    }

}
