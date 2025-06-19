package freeapp.me.todo.presentation.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import freeapp.me.todo.domain.model.Todo
import freeapp.me.todo.domain.repository.TodoRepository
import freeapp.me.todo.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoViewModel(
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private val PAGE_SIZE = 20
    private var currentPage = 1

    private val _uiState = MutableStateFlow(TodoUiState())

    val uiState: StateFlow<TodoUiState> = _uiState.onStart {
        loadInitialTodos()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _uiState.value
    )

    private val _newTodoText = MutableStateFlow("")
    val newTodoText: StateFlow<String> = _newTodoText.asStateFlow()


    init {
        //loadInitialTodos()
    }


    private fun loadInitialTodos() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val initialTodos =
                    todoRepository.getTodosPaginated(currentPage, PAGE_SIZE)
                _uiState.update {
                    it.copy(
                        todos = initialTodos,
                        isLoading = false,
                        isLast = initialTodos.size < PAGE_SIZE
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }


    fun loadMoreTodos() {

        Logger.i("loadMoreTodos", "loadMoreTodos")

        if (_uiState.value.isLoading || _uiState.value.isLast) return


        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                currentPage++
                val newTodos =
                    todoRepository.getTodosPaginated(currentPage, PAGE_SIZE)

                Logger.i("loadMoreTodos", "${newTodos.size}")

                if (newTodos.isEmpty()) {
                    _uiState.update { it.copy(isLast = true, isLoading = false) }
                    return@launch
                }

                _uiState.update {
                    it.copy(
                        todos = it.todos + newTodos,
                        isLoading = false,
                        isLast = newTodos.size < PAGE_SIZE
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }


    /**
     * 새 Todo를 추가합니다.
     * 입력 필드가 비어있지 않은 경우에만 저장합니다.
     */
    fun addTodo() {

        val currentText = _newTodoText.value.trim() // 입력 텍스트 양쪽 공백 제거

        if (currentText.isBlank()) return

        viewModelScope.launch {
            try {
                val newTodo = Todo(text = currentText)
                val insertedTodo = todoRepository.insertTodo(newTodo)
                _newTodoText.value = "" // Todo 추가 후 입력 필드를 초기화합니다.
                _uiState.update { state ->
                    // 최상단에 추가
                    state.copy(todos = listOf(insertedTodo) + state.todos)
                }
                Logger.i("test", "insert todo!! ")
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    /**
     * 새 Todo 입력 필드의 텍스트를 업데이트합니다.
     */
    fun updateNewTodoText(text: String) {
        _newTodoText.value = text
    }


    /**
     * 특정 Todo의 완료 상태를 토글합니다.
     * @param id 토글할 Todo의 고유 ID
     */
    fun toggleTodoStatus(id: Long) {
        viewModelScope.launch {
            try {
                //DB 업데이트
                val updatedTodo =
                    todoRepository.toggleTodoStatus(id)
                //UI 동기화
                _uiState.update { state ->
                    val updatedTodos = state.todos.map { todo ->
                        if (todo.id == updatedTodo.id) updatedTodo else todo
                    }
                    state.copy(todos = updatedTodos)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    /**
     * 특정 Todo를 삭제합니다.
     * @param id 삭제할 Todo의 고유 ID
     */
    fun deleteTodo(id: Long) {
        viewModelScope.launch {
            try {
                todoRepository.deleteTodoById(id)
                _uiState.update { state ->
                    state.copy(todos = state.todos.filter { it.id != id })
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    /**
     * 모든 Todo를 삭제합니다.
     */
    fun clearAllTodos() {
        viewModelScope.launch {
            todoRepository.deleteAllTodos()
            _uiState.update {
                it.copy(todos = emptyList(), isLast = true)
            }
            currentPage = 0
        }
    }

}
