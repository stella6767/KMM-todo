package freeapp.me.todo.model.repository

import freeapp.me.todo.config.db.AppDatabase
import freeapp.me.todo.model.data.PageImpl
import freeapp.me.todo.model.data.Todo
import freeapp.me.todo.model.data.TodoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class TodoRoomRepositoryImpl(
    private val database: AppDatabase,
) : TodoRepository {

    val todoDao = database.todoDao()

    override fun getTodosPaginated(
        currentPage: Int,
        pageSize: Int
    ): Flow<PageImpl<List<Todo>>> {
        val offset = (currentPage - 1) * pageSize
        val paginatedTodosFlow =
            todoDao.getTodosPaginated(pageSize, offset) // 페이지 데이터 Flow
        // 전체 Todo 개수를 가져오는 Flow (suspend 함수를 Flow로 감싸야 함)
        val totalCountFlow = flow {
            emit(todoDao.count().toLong())
        }
        // 두 Flow를 combine하여 TodoResponse를 만듭니다.
        return paginatedTodosFlow.combine(totalCountFlow) { todos, totalCount ->
            val totalPages = (totalCount + pageSize - 1) / pageSize // 총 페이지 수 계산
            val isLastPage = currentPage >= totalPages // 마지막 페이지인지 여부
            PageImpl(
                page = currentPage.toLong(),
                pageSize = pageSize,
                totalCount = totalCount,
                isLast = isLastPage,
                data = todos
            )
        }
    }

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
