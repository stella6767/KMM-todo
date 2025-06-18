package freeapp.me.todo.model.repository

import freeapp.me.todo.config.AppDatabase
import freeapp.me.todo.model.data.Todo
import kotlinx.coroutines.flow.Flow

class TodoRoomRepositoryImpl(
    private val database: AppDatabase,
) : TodoRepository{

    val todoDao = database.todoDao()

    override fun getTodos(): Flow<List<Todo>> {
        return todoDao.getAllAsFlow()
    }

    override suspend fun getTodoById(id: Long): Todo? {
        return todoDao.getById(id)
    }

    override suspend fun insertTodo(todo: Todo) {
        todoDao.insert(todo)
    }

    override suspend fun deleteTodoById(id: Long): Boolean {
        val deleteById = todoDao.deleteById(id)
        return deleteById !== 0
    }

    override suspend fun toggleTodoStatus(id: Long) {
        val todo = getTodoById(id)
        if (todo != null) {
            val updatedTodo = todo.copy(isDone = !todo.isDone)
            todoDao.insert(updatedTodo) // REPLACE 전략이므로 업데이트 역할을 합니다.
        }
    }

    override suspend fun deleteAllTodos() {

        todoDao.deleteAll()
    }

}
