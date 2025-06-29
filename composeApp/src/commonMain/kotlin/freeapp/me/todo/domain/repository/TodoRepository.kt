package freeapp.me.todo.domain.repository

import freeapp.me.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun getTodos(): Flow<List<Todo>>

    suspend fun getTodosPaginated(currentPage: Int, pageSize: Int):  List<Todo>

    suspend fun getTodoById(id: Long): Todo?

    suspend fun insertTodo(todo: Todo): Todo

    suspend fun deleteTodoById(id: Long): Boolean

    suspend fun toggleTodoStatus(id: Long) : Todo

    suspend fun deleteAllTodos()
}
